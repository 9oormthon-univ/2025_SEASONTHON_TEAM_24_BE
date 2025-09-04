package com.qoormthon.empty_wallet.domain.character.service;

import com.qoormthon.empty_wallet.domain.character.entity.CharCode;
import com.qoormthon.empty_wallet.domain.character.entity.Score;
import com.qoormthon.empty_wallet.domain.character.repository.ScoreRepository;
import com.qoormthon.empty_wallet.domain.survey.dto.request.SubmitSurveyRequest;
import com.qoormthon.empty_wallet.domain.survey.entity.SurveyOption;
import com.qoormthon.empty_wallet.domain.survey.entity.SurveyType;
import com.qoormthon.empty_wallet.domain.survey.repository.SurveyOptionRepository;
import com.qoormthon.empty_wallet.domain.user.entity.User;
import com.qoormthon.empty_wallet.domain.user.repository.UserRepository;
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

    @Transactional
    public void applySurvey(Long userId, SubmitSurveyRequest req) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        // 1) 각 캐릭터별 합산 버킷
        EnumMap<CharCode, Long> sum = new EnumMap<>(CharCode.class);
        for (CharCode c : CharCode.values()) sum.put(c, 0L);

        // 2) 답변 → 옵션 조회 → 코드/가중치 해석 → 합산
        Set<Long> answeredSurveyIds = new HashSet<>();
        for (SubmitSurveyRequest.Answer a : req.answers()) {
            SurveyOption opt = optionRepo.findBySurveyIdAndType(a.surveyId(), a.optionType())
                    .orElseThrow(() -> new IllegalArgumentException("OPTION_NOT_FOUND"));
            answeredSurveyIds.add(a.surveyId());

            // "CAF" or "TAX,CAF"
            String[] codes = opt.getCode().split(",");
            int weight = opt.getWeight(); // ex) 2 or 21 or -11

            int sign = weight < 0 ? -1 : 1;
            String digits = String.valueOf(Math.abs(weight)); // "21"
            // 자리수가 부족하면 왼쪽 0 패딩
            if (digits.length() < codes.length) {
                digits = String.format("%0" + codes.length + "d", Integer.parseInt(digits));
            }

            for (int i = 0; i < codes.length; i++) {
                CharCode code = CharCode.of(codes[i]);
                int d = (i < digits.length()) ? (digits.charAt(i) - '0') : 0;
                long delta = sign * d;
                sum.put(code, sum.get(code) + delta);
            }
        }

        // 3) 풀서베이 동점 보정(+1) 규칙
        if (req.type() == SurveyType.FULL) {
            // 최대값 집합
            long max = sum.values().stream().mapToLong(v -> v).max().orElse(0L);
            List<CharCode> tied = new ArrayList<>();
            for (var e : sum.entrySet()) if (e.getValue() == max) tied.add(e.getKey());

            if (tied.size() >= 2) {
                // Q7 있었으면 YOLO +1
                if (answeredSurveyIds.contains(7L)) {
                    sum.put(CharCode.YOLO, sum.get(CharCode.YOLO) + 1);
                }
                // Q3 또는 Q9 있었으면 FASH/IMP +1
                if (answeredSurveyIds.contains(3L) || answeredSurveyIds.contains(9L)) {
                    sum.put(CharCode.FASH, sum.get(CharCode.FASH) + 1);
                    sum.put(CharCode.IMP, sum.get(CharCode.IMP) + 1);
                }
                // (그래도 동점이면 'CAF > TAX > FASH > IMP > SUB > YOLO' 우선순위는
                // 다음 이슈의 "최고 점수 캐릭터 반환" 단계에서만 사용)
            }
        }

        // 4) 점수 테이블에 반영 (FULL=덮어쓰기, QUICK=가산)
        Score bucket = scoreRepo.findByUser(user)
                .orElseGet(() -> scoreRepo.save(Score.of(user)));

        // FULL = 덮어쓰기
                bucket.overwrite(
                        sum.get(CharCode.CAF),
                        sum.get(CharCode.TAX),
                        sum.get(CharCode.IMP),
                        sum.get(CharCode.SUB),
                        sum.get(CharCode.YOLO),
                        sum.get(CharCode.FASH)
                );

        // QUICK = 누적 가산
                bucket.addCaf(sum.get(CharCode.CAF));
                bucket.addTax(sum.get(CharCode.TAX));
                bucket.addImp(sum.get(CharCode.IMP));
                bucket.addSub(sum.get(CharCode.SUB));
                bucket.addYolo(sum.get(CharCode.YOLO));
                bucket.addFash(sum.get(CharCode.FASH));
                scoreRepo.save(bucket);
    }

    private void setAll(Score s, EnumMap<CharCode, Long> m) {
        s.addCaf(-s.getCaf());  s.addTax(-s.getTax());  s.addImp(-s.getImp());
        s.addSub(-s.getSub());  s.addYolo(-s.getYolo()); s.addFash(-s.getFash());
        addAll(s, m);
    }

    private void addAll(Score s, EnumMap<CharCode, Long> m) {
        s.addCaf(m.get(CharCode.CAF));
        s.addTax(m.get(CharCode.TAX));
        s.addImp(m.get(CharCode.IMP));
        s.addSub(m.get(CharCode.SUB));
        s.addYolo(m.get(CharCode.YOLO));
        s.addFash(m.get(CharCode.FASH));
    }
}
