package com.qoormthon.empty_wallet.global.exception;

public class ValidationException extends CustomException {

  public ValidationException(ErrorCode errorCode) {
    super(errorCode);
  }
}
