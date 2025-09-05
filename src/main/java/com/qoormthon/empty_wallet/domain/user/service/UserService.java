package com.qoormthon.empty_wallet.domain.user.service;

import com.qoormthon.empty_wallet.domain.user.dto.RequiredDaysRequest;
import com.qoormthon.empty_wallet.domain.user.dto.RequiredDaysResponse;
import com.qoormthon.empty_wallet.domain.user.entity.User;
import com.qoormthon.empty_wallet.domain.user.repository.UserRepository;
import com.qoormthon.empty_wallet.global.exception.ErrorCode;
import com.qoormthon.empty_wallet.global.exception.InvalidValueException;
import com.qoormthon.empty_wallet.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;

  /**
   * 목표 금액까지 걸리는 예상 일수를 계산합니다.
   * @param requiredDaysRequest 월 수익, 월 고정지출을 포함한 요청 객체
   * @param httpServletRequest HTTP 요청 객체
   * @return 목표 금액까지 걸리는 예상 일수 (소수점 첫째 자리까지 표시)
   */
  @Transactional
  public double getDaysToGoal(RequiredDaysRequest requiredDaysRequest, HttpServletRequest httpServletRequest) {

    Long targetPrice = requiredDaysRequest.getTargetPrice();
    Long monthlyPay = requiredDaysRequest.getMonthlyPay();

    String accessToken = jwtTokenProvider.extractToken(httpServletRequest);
    Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

    User user = userRepository.findById(userId).orElse(null);

    double savingMoney = (monthlyPay)/10.0; // 한달 저축 금액
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


    // 유저가 존재하지 않을 경우 예외 처리
    if(userRepository.findById(userId).isEmpty()) {
      log.error("존재하지 않는 유저입니다.");
      throw new InvalidValueException(ErrorCode.INVALID_INPUT_VALUE);
    }

    // 목표 금액까지 남은 일수 저장
    user.setDaysToGoal(requiredDaysRequest);


    return roundedDays;
  }

}
