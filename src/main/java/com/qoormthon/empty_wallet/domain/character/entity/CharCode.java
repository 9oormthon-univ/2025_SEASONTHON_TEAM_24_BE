package com.qoormthon.empty_wallet.domain.character.entity;

public enum CharCode {
    CAF, TAX, IMP, SUB, YOLO, FASH;
    public static CharCode of(String raw) {
        String k = raw.trim().toUpperCase();
        return CharCode.valueOf(k);
    }
}