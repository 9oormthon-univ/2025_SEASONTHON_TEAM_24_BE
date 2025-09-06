package com.qoormthon.empty_wallet.domain.survey.dto.request;

import com.qoormthon.empty_wallet.domain.survey.entity.SurveyType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

// 사용자가 선택한 답안 제출 요청
public record SubmitSurveyRequest(

        @NotNull(message = "type은 필수입니다.")
        SurveyType type,                 // FULL | QUICK

        @NotEmpty(message = "answers는 비어있을 수 없습니다")
        List<@Valid Answer> answers          // 질문ID + 보기 타입(A~F)
) {
    public record Answer(Long surveyId, String optionType) {}
}