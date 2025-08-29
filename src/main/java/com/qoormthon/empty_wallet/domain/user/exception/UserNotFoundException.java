package com.qoormthon.empty_wallet.domain.user.exception;


import com.qoormthon.empty_wallet.global.exception.CustomException;
import com.qoormthon.empty_wallet.global.exception.ErrorCode;

public class UserNotFoundException extends CustomException {

  public UserNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }

}
