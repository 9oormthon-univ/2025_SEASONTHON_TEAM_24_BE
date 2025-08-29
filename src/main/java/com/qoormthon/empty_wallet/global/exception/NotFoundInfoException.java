package com.qoormthon.empty_wallet.global.exception;

public class NotFoundInfoException extends CustomException {

  public NotFoundInfoException(ErrorCode errorCode) {
    super(errorCode);
  }
}
