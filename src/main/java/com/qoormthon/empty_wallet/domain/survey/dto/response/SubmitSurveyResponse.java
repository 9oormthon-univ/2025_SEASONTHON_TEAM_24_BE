package com.qoormthon.empty_wallet.domain.survey.dto.response;

public record SubmitSurveyResponse(
        boolean completed,   // 모든 필수 문항 완료 여부
        int savedCount,       // 저장/업서트된 답안 수
        String code,
        String name,
        String desc,
        String trait,
        Integer savings
){
    public static SubmitSurveyResponse of(boolean completed, int savedCount,
                                          String code, String name, String desc, String trait, Integer savings) {
        return new SubmitSurveyResponse(completed, savedCount, code, name, desc, trait, savings);
    }
}