package com.qoormthon.empty_wallet.global.exception;

public class InvalidValueException extends CustomException {

  public InvalidValueException(ErrorCode errorCode) {
    super(errorCode);
  }
}
