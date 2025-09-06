package com.qoormthon.empty_wallet.global.exception;

public class SurveyValidationException extends CustomException {
    private final String detail;

    public SurveyValidationException(ErrorCode errorCode, String detail) {
        super(errorCode);
        this.detail = detail;
    }

    @Override
    public String getMessage() {
        // 기본 메시지 + 상세 사유
        String base = super.getMessage(); // errorCode.getMessage()
        return (detail == null || detail.isBlank()) ? base : base + " (" + detail + ")";
    }

    public String getDetail() {
        return detail;
    }
}