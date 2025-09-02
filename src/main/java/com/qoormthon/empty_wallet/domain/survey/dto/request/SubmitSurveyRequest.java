package com.qoormthon.empty_wallet.domain.survey.dto.request;

import com.qoormthon.empty_wallet.domain.survey.entity.SurveyType;

import java.util.List;

// 사용자가 선택한 답안 제출 요청
public record SubmitSurveyRequest(
        SurveyType type,                 // FULL | QUICK
        List<Answer> answers          // 질문ID + 보기 타입(A~F)
) {
    public record Answer(Long surveyId, String optionType) {}
}