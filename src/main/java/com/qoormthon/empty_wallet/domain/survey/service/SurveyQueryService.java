package com.qoormthon.empty_wallet.domain.survey.service;

import com.qoormthon.empty_wallet.domain.survey.dto.response.*;
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

// 설문 조회 전용 서비스.
@Service
@RequiredArgsConstructor
public class SurveyQueryService {

    private final SurveyRepository surveyRepo;
    private final SurveyOptionRepository optionRepo;

    @Transactional(readOnly = true)
    public SurveyBundleResponse getSurveyBundle(SurveyType type) {
        List<Survey> questions = surveyRepo.findByTypeOrderByIdAsc(type);
        if (questions.isEmpty()) {
            return new SurveyBundleResponse(type, 0, List.of());
        }

        List<Long> qIds = questions.stream().map(Survey::getId).toList();
        List<SurveyOption> allOptions = optionRepo.findBySurveyIdInOrderBySurveyIdAscTypeAsc(qIds);

        Map<Long, List<SurveyOption>> byQuestion =
                allOptions.stream().collect(Collectors.groupingBy(SurveyOption::getSurveyId, LinkedHashMap::new, Collectors.toList()));

        List<QuestionResponse> qDtos = questions.stream().map(q -> {
            List<OptionResponse> opts = byQuestion.getOrDefault(q.getId(), List.of()).stream()
                    .map(o -> new OptionResponse(o.getType(), o.getTitle()))
                    .toList();
            return new QuestionResponse(q.getId(), q.getTitle(), opts);
        }).toList();

        return new SurveyBundleResponse(type, qDtos.size(), qDtos);
    }
}
