package com.qoormthon.empty_wallet.global.exception;

public class InternalServerException extends CustomException {

  public InternalServerException(ErrorCode errorCode) {
    super(errorCode);
  }
}
