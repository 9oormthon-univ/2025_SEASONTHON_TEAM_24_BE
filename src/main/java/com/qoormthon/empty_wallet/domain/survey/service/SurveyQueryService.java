package com.qoormthon.empty_wallet.domain.survey.service;

import com.qoormthon.empty_wallet.domain.survey.dto.response.OptionResponse;
import com.qoormthon.empty_wallet.domain.survey.dto.response.QuestionResponse;
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
public class SurveyQueryService {

    private static final Set<String> CHARACTER_CODES =
            Set.of("CAF","TAX","FAS","SUB","IMP","YOLO");

    private final SurveyRepository surveyRepo;
    private final SurveyOptionRepository optionRepo;

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

            // 캐릭터 문항(Q3)만 필터링 대상으로 간주
            boolean isCharacterQuestion = (type == SurveyType.QUICK) && looksLikeCharacterQuestion(q, raw);

            // 캐릭터 문항(Q3)인데 캐릭터 코드가 없으면 이 문항은 제외 (Q1/Q2만 내려감)
            if (isCharacterQuestion && (characterCodeOrNull == null || characterCodeOrNull.isBlank())) {
                continue;
            }

            List<SurveyOption> filtered = raw;
            if (isCharacterQuestion) {
                String target = characterCodeOrNull.trim().toUpperCase();
                filtered = raw.stream()
                        .filter(o -> target.equalsIgnoreCase(safeUpper(o.getCode())))
                        .toList();
                if (filtered.isEmpty()) continue; // 방어: 매칭 없으면 이 문항 스킵
            }

            // Q1, Q2 등은 code가 있어도 무시하고 전부 노출 (그대로 filtered=raw)
            List<OptionResponse> opts = filtered.stream()
                    .map(o -> new OptionResponse(o.getType(), o.getTitle()))
                    .toList();

            qDtos.add(new QuestionResponse(q.getId(), q.getTitle(), opts));
        }

        return new SurveyBundleResponse(type, qDtos.size(), qDtos);
    }

    // 판별: 타이틀에 '캐릭터' 포함 + code가 캐릭터코드 집합에 속하며 2종 이상 존재
    private boolean looksLikeCharacterQuestion(Survey question, List<SurveyOption> options) {
        String title = Optional.ofNullable(question.getTitle()).orElse("");
        if (!title.contains("캐릭터")) return false; // ← Q2는 여기서 걸러짐

        Set<String> codes = options.stream()
                .map(SurveyOption::getCode)
                .map(this::safeUpper)
                .filter(c -> c != null && !c.isBlank())
                .collect(Collectors.toSet());

        if (codes.size() < 2) return false; // 한두 개 실수 입력은 무시
        // 전부 허용 코드여야 Q3로 인정
        return codes.stream().allMatch(CHARACTER_CODES::contains);
    }

    private String safeUpper(String s) {
        return (s == null) ? null : s.toUpperCase();
    }
}
