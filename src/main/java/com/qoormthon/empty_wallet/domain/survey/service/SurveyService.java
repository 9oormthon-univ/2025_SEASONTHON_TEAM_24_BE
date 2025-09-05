package com.qoormthon.empty_wallet.domain.survey.service;

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

    private final SurveyRepository surveyRepo;
    private final SurveyCommandService commandService; // 검증 로직
    private final SurveyOptionRepository optionRepo;

    private final CharacterScoreService characterScoreService;

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
        var checked = commandService.submit(req); // completed(), savedCount()

        // 2) 점수 반영 (FULL=덮어쓰기, QUICK=가산; 동점 보정은 내부 규칙)
        characterScoreService.applySurvey(userId, req);

        // 3) 최고 캐릭터 선정 & 회원.character 매핑
        var selected = characterScoreService.mapTopCharacter(userId);

        // 4) 응답: 캐릭터 코드/이름/설명까지 포함
        return SubmitSurveyResponse.of(
                checked.completed(),
                checked.savedCount(),
                selected.getCode(),
                selected.getName(),
                selected.getDescription(),
                selected.getTrait()

        );
    }
    // =====================[ 내부 유틸 ]=====================
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

    private String safeUpper(String s) {
        return (s == null) ? null : s.toUpperCase();
    }
}
