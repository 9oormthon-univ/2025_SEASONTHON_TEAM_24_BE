package com.qoormthon.empty_wallet.domain.user.exception;


import com.qoormthon.empty_wallet.global.exception.CustomException;
import com.qoormthon.empty_wallet.global.exception.ErrorCode;

public class UserDuplicationException extends CustomException {

  public UserDuplicationException(ErrorCode errorCode) {
    super(errorCode);
  }

}
