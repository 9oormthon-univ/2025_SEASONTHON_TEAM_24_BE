package com.qoormthon.empty_wallet.domain.survey.dto.response;

import java.util.List;

public record QuestionResponse(
        Long surveyId,
        String title,
        List<OptionResponse> options
) {}