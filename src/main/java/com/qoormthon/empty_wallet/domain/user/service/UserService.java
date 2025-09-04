package com.qoormthon.empty_wallet.domain.user.service;

import com.qoormthon.empty_wallet.domain.user.dto.RequiredDaysResponse;
import com.qoormthon.empty_wallet.global.exception.ErrorCode;
import com.qoormthon.empty_wallet.global.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  /**
   * 목표 금액까지 걸리는 예상 일수를 계산합니다.
   * @param targetPrice 목표금액
   * @param monthlyPay 월 수익
   * @param monthlyCost 월 고정지출
   * @return 목표 금액까지 걸리는 예상 일수 (소수점 첫째 자리까지 표시)
   */
  public double getDaysToGoal(Long targetPrice, Long monthlyPay, Long monthlyCost) {
    double savingMoney = (monthlyPay-monthlyCost)/10.0; // 하루 저축 금액
    double days = ((double)targetPrice/(savingMoney))*30;
    double roundedDays = Math.round(days); // 소수점 반올림하도록 수정

    // 계산 결과가 음수일 경우 예외 처리
    if(roundedDays < 0) {
      log.error("계산 결과가 음수입니다. 입력 값을 확인해주세요.");
      throw new InvalidValueException(ErrorCode.INVALID_INPUT_VALUE);
    }

    // 계산 결과가 100년 이상일 경우 예외 처리
    if(roundedDays > 36525) {
      log.error("계산 결과가 100년 이상 입니다. 입력 값을 확인해주세요.");
      throw new InvalidValueException(ErrorCode.INVALID_INPUT_VALUE);
    }

    return roundedDays;
  }

}
