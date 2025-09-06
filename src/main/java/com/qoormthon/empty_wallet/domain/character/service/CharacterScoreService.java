package com.qoormthon.empty_wallet.domain.character.service;

import com.qoormthon.empty_wallet.domain.character.entity.CharCode;
import com.qoormthon.empty_wallet.domain.character.entity.Character;
import com.qoormthon.empty_wallet.domain.character.entity.Score;
import com.qoormthon.empty_wallet.domain.character.repository.CharacterRepository;
import com.qoormthon.empty_wallet.domain.character.repository.ScoreRepository;
import com.qoormthon.empty_wallet.domain.survey.dto.request.SubmitSurveyRequest;
import com.qoormthon.empty_wallet.domain.survey.entity.SurveyOption;
import com.qoormthon.empty_wallet.domain.survey.entity.SurveyType;
import com.qoormthon.empty_wallet.domain.survey.repository.SurveyOptionRepository;
import com.qoormthon.empty_wallet.domain.survey.service.CharacterResolverService;
import com.qoormthon.empty_wallet.domain.user.entity.User;
import com.qoormthon.empty_wallet.domain.user.repository.UserRepository;
import com.qoormthon.empty_wallet.global.exception.ErrorCode;
import com.qoormthon.empty_wallet.global.exception.NotFoundInfoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CharacterScoreService {

    private final SurveyOptionRepository optionRepo;
    private final ScoreRepository scoreRepo;
    private final UserRepository userRepo;
    private final CharacterResolverService characterResolver;
    private final CharacterRepository characterRepo;

    private static final List<CharCode> PRIORITY = List.of(
            CharCode.CAF, CharCode.TAX, CharCode.FASH, CharCode.IMP, CharCode.SUB, CharCode.YOLO
    );

    private static final long QUICK_Q1_SURVEY_ID = 11L;


    @Transactional
    public void applySurvey(Long userId, SubmitSurveyRequest req) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundInfoException(ErrorCode.USER_NOT_FOUND));

        final String userChar = characterResolver.resolve(null, userId); // ex) "CAF","YOLO",...

        EnumMap<CharCode, Long> sum = new EnumMap<>(CharCode.class);
        for (CharCode c : CharCode.values()) sum.put(c, 0L);

        // 어떤 문항에서 무슨 보기 선택했는지 추적 (동점 보정 조건에 필요)
        Map<Long, String> picked = new HashMap<>();
        Set<Long> answeredSurveyIds = new HashSet<>();

        for (SubmitSurveyRequest.Answer a : req.answers()) {

            SurveyOption opt;
            if (req.type() == SurveyType.QUICK) {
                List<SurveyOption> candidates = optionRepo.findAllBySurveyIdAndType(a.surveyId(), a.optionType());
                if (candidates.isEmpty()) {
                    throw new NotFoundInfoException(ErrorCode.OPTION_NOT_FOUND);
                }

                opt = candidates.stream()
                        .filter(o -> userChar != null
                                && userChar.equalsIgnoreCase(Objects.toString(o.getCode(), "")))
                        .findFirst()
                        .orElseGet(() -> candidates.stream()
                                .filter(o -> o.getCode() == null || o.getCode().isBlank())
                                .findFirst()
                                .orElse(candidates.get(0)));
            } else {
                opt = optionRepo.findBySurveyIdAndType(a.surveyId(), a.optionType())
                        .orElseThrow(() -> new NotFoundInfoException(ErrorCode.OPTION_NOT_FOUND));
            }

            answeredSurveyIds.add(a.surveyId());
            picked.put(a.surveyId(), a.optionType()); // ← 선택 보기 저장

            String rawCode = opt.getCode();

            // QUICK Q1 보정
            if (rawCode == null || rawCode.isBlank()) {
                // code가 없는 일반 문항인데, QUICK Q1이면 사용자 캐릭터 버킷에 weight 가산
                if (req.type() == SurveyType.QUICK && isQuickQ1(a.surveyId()) && userChar != null && !userChar.isBlank()) {
                    int w = Optional.ofNullable(opt.getWeight()).orElse(0); // -2 ~ +2
                    CharCode code = CharCode.of(userChar);
                    sum.put(code, sum.get(code) + w);
                }
                // QUICK Q1이 아니거나 사용자 캐릭터를 모르면 스킵
                continue;
            }

            // 다코드/단일코드 처리
            String[] codes = Arrays.stream(rawCode.split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).toArray(String[]::new);
            if (codes.length == 0) continue;

            int weight = Optional.ofNullable(opt.getWeight()).orElse(0);
            int sign = Integer.signum(weight);
            int abs = Math.abs(weight);

            if (codes.length == 1) {
                CharCode code = CharCode.of(codes[0]);
                sum.put(code, sum.get(code) + (long) sign * abs);
            } else {
                applyMultiCode(sum, codes, weight);
            }
        }

        // 2) FULL 동점 보정 — "A"를 선택했을 때만 +1
        if (req.type() == SurveyType.FULL) {
            long max = sum.values().stream().mapToLong(v -> v).max().orElse(0L);

            // 최고점 동점 집합
            Set<CharCode> leaders = new HashSet<>();
            for (Map.Entry<CharCode, Long> e : sum.entrySet()) {
                if (e.getValue() == max) leaders.add(e.getKey());
            }

            if (leaders.size() >= 2) { // 진짜 '최고점' 동점일 때만 보정
                String pick3 = picked.get(3L);
                String pick7 = picked.get(7L);
                String pick9 = picked.get(9L);

                // Q7=A → YOLO +1 (단, YOLO가 leaders 안에 있을 때만)
                if ("A".equalsIgnoreCase(pick7) && leaders.contains(CharCode.YOLO)) {
                    sum.put(CharCode.YOLO, sum.get(CharCode.YOLO) + 1);
                }

                // Q3=A → FASH/IMP 각각 +1 (단, 각 코드가 leaders 안에 있을 때만)
                if ("A".equalsIgnoreCase(pick3)) {
                    if (leaders.contains(CharCode.FASH)) sum.put(CharCode.FASH, sum.get(CharCode.FASH) + 1);
                    if (leaders.contains(CharCode.IMP))  sum.put(CharCode.IMP,  sum.get(CharCode.IMP)  + 1);
                }

                // Q9=A → FASH/IMP 각각 +1 (단, 각 코드가 leaders 안에 있을 때만)
                if ("A".equalsIgnoreCase(pick9)) {
                    if (leaders.contains(CharCode.FASH)) sum.put(CharCode.FASH, sum.get(CharCode.FASH) + 1);
                    if (leaders.contains(CharCode.IMP))  sum.put(CharCode.IMP,  sum.get(CharCode.IMP)  + 1);
                }
            }
        }

        // 3) 점수 반영 (FULL=덮어쓰기, QUICK=누적)
        Score bucket = scoreRepo.findByUser(user)
                .orElseGet(() -> scoreRepo.save(Score.of(user)));

        if (req.type() == SurveyType.FULL) {
            bucket.overwrite(
                    sum.get(CharCode.CAF),
                    sum.get(CharCode.TAX),
                    sum.get(CharCode.IMP),
                    sum.get(CharCode.SUB),
                    sum.get(CharCode.YOLO),
                    sum.get(CharCode.FASH)
            );
        } else { // QUICK
            bucket.addCaf(sum.get(CharCode.CAF));
            bucket.addTax(sum.get(CharCode.TAX));
            bucket.addImp(sum.get(CharCode.IMP));
            bucket.addSub(sum.get(CharCode.SUB));
            bucket.addYolo(sum.get(CharCode.YOLO));
            bucket.addFash(sum.get(CharCode.FASH));
        }

        scoreRepo.save(bucket);
    }

    @Transactional
    public Character mapTopCharacter(Long userId) {
        User  user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundInfoException(ErrorCode.USER_NOT_FOUND));

        Score s = scoreRepo.findByUser(user)
                .orElseThrow(() -> new NotFoundInfoException(ErrorCode.SCORE_NOT_FOUND));

        long max = Math.max(Math.max(Math.max(s.getCaf(), s.getTax()), Math.max(s.getImp(), s.getSub())),
                Math.max(s.getYolo(), s.getFash()));

        CharCode chosen = null;
        for (CharCode code : PRIORITY) {
            long v = switch (code) {
                case CAF -> s.getCaf();
                case TAX -> s.getTax();
                case IMP -> s.getImp();
                case SUB -> s.getSub();
                case YOLO -> s.getYolo();
                case FASH -> s.getFash();
            };
            if (v == max) { chosen = code; break; }
        }
        if (chosen == null) chosen = CharCode.CAF;

        final CharCode resolved = chosen;
        final String code = resolved.name();

        Character character = characterRepo.findByCode(code)
                .orElseThrow(() -> new NotFoundInfoException(ErrorCode.CHARACTER_ROW_NOT_FOUND));

        user.setCharacter(character);
        userRepo.save(user);
        return character;
    }

    // --- [A] 다코드 weight 분배: 항상 자릿수 매핑 + 왼쪽 0패딩 ---
    private void applyMultiCode(EnumMap<CharCode, Long> sum, String[] codes, int weight) {
        int sign = Integer.signum(weight);
        int abs  = Math.abs(weight);

        // codes 길이에 맞춰 왼쪽 0 패딩 (예: abs=12, codes=3개 -> "012")
        String digits = String.format("%0" + codes.length + "d", abs);

        for (int i = 0; i < codes.length; i++) {
            int d = digits.charAt(i) - '0';
            if (d == 0) continue;
            CharCode code = CharCode.of(codes[i]);
            sum.put(code, sum.get(code) + (long) sign * d);
        }
    }

    private boolean isQuickQ1(Long surveyId) {
        return Objects.equals(surveyId, QUICK_Q1_SURVEY_ID);
    }
}
