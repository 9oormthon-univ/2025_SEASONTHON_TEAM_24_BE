package com.qoormthon.empty_wallet.domain.survey.dto.response;

import com.qoormthon.empty_wallet.domain.survey.entity.SurveyType;

import java.util.List;

// 풀, 퀵 한 묶음으로 전달
public record SurveyBundleResponse(
        SurveyType type,
        int questionCount,
        List<QuestionResponse> questions
) {}