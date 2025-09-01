package com.qoormthon.empty_wallet.domain.auth.exception;

import com.qoormthon.empty_wallet.global.exception.CustomException;
import com.qoormthon.empty_wallet.global.exception.ErrorCode;

public class TokenExtractionException extends CustomException {

  public TokenExtractionException(ErrorCode errorCode) {
    super(errorCode);
  }
}
