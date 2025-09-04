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
        // 점수 계산 & 반영 (FULL=덮어쓰기, QUICK=누적 + 동점 보정은 CharacterScoreService 내부 규칙대로)
        characterScoreService.applySurvey(userId, req);

        // completed 계산(선택): 해당 타입의 질문 개수와 요청 내 고유 질문ID 수 비교
        int totalQuestions = surveyRepo.findByTypeOrderByIdAsc(req.type()).size();
        int uniqueAnswered = (int) req.answers().stream()
                .map(SubmitSurveyRequest.Answer::surveyId)
                .distinct()
                .count();
        boolean completed = (uniqueAnswered == totalQuestions && totalQuestions > 0);

        return new SubmitSurveyResponse(completed, req.answers().size());
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
