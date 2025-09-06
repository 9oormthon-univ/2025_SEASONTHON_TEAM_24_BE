package com.qoormthon.empty_wallet.domain.survey.service;

import com.qoormthon.empty_wallet.domain.character.entity.Character;
import com.qoormthon.empty_wallet.domain.character.service.CharacterScoreService;
import com.qoormthon.empty_wallet.domain.survey.dto.request.SubmitSurveyRequest;
import com.qoormthon.empty_wallet.domain.survey.dto.response.OptionResponse;
import com.qoormthon.empty_wallet.domain.survey.dto.response.QuestionResponse;
import com.qoormthon.empty_wallet.domain.survey.dto.response.SubmitSurveyResponse;
import com.qoormthon.empty_wallet.domain.survey.dto.response.SurveyBundleResponse;
import com.qoormthon.empty_wallet.domain.survey.entity.Survey;
import com.qoormthon.empty_wallet.domain.survey.entity.SurveyOption;
import com.qoormthon.empty_wallet.domain.survey.entity.SurveyType;
import com.qoormthon.empty_wallet.domain.survey.repository.SurveyOptionRepository;
import com.qoormthon.empty_wallet.domain.survey.repository.SurveyRepository;
import com.qoormthon.empty_wallet.domain.user.entity.User;
import com.qoormthon.empty_wallet.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private static final Set<String> CHARACTER_CODES =
            Set.of("CAF","TAX","FASH","SUB","IMP","YOLO");

    // 캐릭터별 월 추가 저축액(원)
    private static final Map<String, Long> MONTHLY_SAVINGS_BY_CHAR = Map.of(
            "CAF", 103_600L,
            "TAX", 420_000L,
            "IMP", 420_000L,
            "SUB",  70_000L,
            "YOLO", 840_000L,
            "FASH", 700_000L
    );

    private final SurveyRepository surveyRepo;
    private final SurveyCommandService commandService; // 검증 로직
    private final SurveyOptionRepository optionRepo;

    private final CharacterScoreService characterScoreService;

    private final UserRepository userRepository;

    // =====================[ 조회 ]=====================
    @Transactional(readOnly = true)
    public SurveyBundleResponse getSurveyBundle(SurveyType type) {
        return getSurveyBundle(type, null);
    }

    @Transactional(readOnly = true)
    public SurveyBundleResponse getSurveyBundle(SurveyType type, String characterCodeOrNull) {
        List<Survey> questions = surveyRepo.findByTypeOrderByIdAsc(type);
        if (questions.isEmpty()) {
            return new SurveyBundleResponse(type, 0, List.of());
        }

        List<Long> qIds = questions.stream().map(Survey::getId).toList();
        List<SurveyOption> allOptions =
                optionRepo.findBySurveyIdInOrderBySurveyIdAscTypeAsc(qIds);

        Map<Long, List<SurveyOption>> byQuestion = allOptions.stream()
                .collect(Collectors.groupingBy(SurveyOption::getSurveyId,
                        LinkedHashMap::new, Collectors.toList()));

        List<QuestionResponse> qDtos = new ArrayList<>();

        for (Survey q : questions) {
            List<SurveyOption> raw = byQuestion.getOrDefault(q.getId(), List.of());

            // QUICK에서 캐릭터 문항(Q3)만 필터
            boolean isCharacterQuestion = (type == SurveyType.QUICK) && looksLikeCharacterQuestion(q, raw);

            if (isCharacterQuestion && (characterCodeOrNull == null || characterCodeOrNull.isBlank())) {
                // Q3인데 캐릭터코드 미지정 → 스킵 (Q1/Q2만 내려감)
                continue;
            }

            List<SurveyOption> filtered = raw;
            if (isCharacterQuestion) {
                String target = characterCodeOrNull.trim().toUpperCase();
                filtered = raw.stream()
                        .filter(o -> target.equalsIgnoreCase(safeUpper(o.getCode())))
                        .toList();
                if (filtered.isEmpty()) continue;
            }

            List<OptionResponse> opts = filtered.stream()
                    .map(o -> new OptionResponse(o.getType(), o.getTitle()))
                    .toList();

            qDtos.add(new QuestionResponse(q.getId(), q.getTitle(), opts));
        }

        return new SurveyBundleResponse(type, qDtos.size(), qDtos);
    }

    // =====================[ 제출: 응답 미저장, 점수만 반영 ]=====================
    @Transactional
    public SubmitSurveyResponse submit(Long userId, SubmitSurveyRequest req) {

        // 1) 검증(저장 없음): 유효한 답 개수 및 완료 여부 계산
        SubmitSurveyResponse  checked = commandService.submit(req); // completed(), savedCount()

        // 2) 점수 반영 (FULL=덮어쓰기, QUICK=가산; 동점 보정은 내부 규칙)
        characterScoreService.applySurvey(userId, req);

        // 3) 최고 캐릭터 선정 & 회원.character 매핑
        Character selected = characterScoreService.mapTopCharacter(userId);

        // FULL에서만 savings 계산
        Integer savingsDays = null;
        if (req.type() == SurveyType.FULL) {
            savingsDays = calculateSavingsDays(userId, selected.getCode());
        }

        // 4) 응답: 캐릭터 코드/이름/설명까지 포함
        return SubmitSurveyResponse.of(
                checked.completed(),
                checked.savedCount(),
                selected.getCode(),
                selected.getName(),
                selected.getDescription(),
                selected.getTrait(),
                savingsDays

        );
    }
    // 유틸 함수
    private boolean looksLikeCharacterQuestion(Survey question, List<SurveyOption> options) {
        String title = Optional.ofNullable(question.getTitle()).orElse("");
        if (!title.contains("캐릭터")) return false;

        Set<String> codes = options.stream()
                .map(SurveyOption::getCode)
                .map(this::safeUpper)
                .filter(c -> c != null && !c.isBlank())
                .collect(Collectors.toSet());

        if (codes.size() < 2) return false;
        return codes.stream().allMatch(CHARACTER_CODES::contains);
    }

    private Integer calculateSavingsDays(Long userId, String characterCode) {
        // 유저 조회
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return 0;

        long monthlyPay = Optional.ofNullable(user.getMonthlyPay()).orElse(0L);
        Long targetPrice = safeTargetPrice(user); // = user.getTargetPrice()
        if (targetPrice == null || targetPrice <= 0) return 0;

        // 기본 월저축액 = 월수익의 10%
        long baseMonthlySave = Math.max(0L, Math.round(monthlyPay * 0.10));
        if (baseMonthlySave <= 0) return 0;

        long extra = Math.max(0L, MONTHLY_SAVINGS_BY_CHAR.getOrDefault(characterCode, 0L));
        long withStrategy = baseMonthlySave + extra;
        if (withStrategy <= 0) return 0;

        long monthsBaseline = (long) Math.ceil((double) targetPrice / baseMonthlySave);
        long monthsWithStrat = (long) Math.ceil((double) targetPrice / withStrategy);

        long monthsSaved = Math.max(0L, monthsBaseline - monthsWithStrat);
        long daysSaved = monthsSaved * 28; // 28일 가정

        return (int) Math.min(Integer.MAX_VALUE, daysSaved);
    }

    private Long safeTargetPrice(User user) {
        return user.getTargetPrice();
    }

    private String safeUpper(String s) {
        return (s == null) ? null : s.toUpperCase();
    }
}
