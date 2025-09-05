package com.qoormthon.empty_wallet.domain.user.controller;

import com.qoormthon.empty_wallet.domain.user.docs.UserDocs;
import com.qoormthon.empty_wallet.domain.user.dto.RequiredDaysRequest;
import com.qoormthon.empty_wallet.domain.user.dto.RequiredDaysResponse;
import com.qoormthon.empty_wallet.domain.user.service.UserService;
import com.qoormthon.empty_wallet.global.common.dto.response.ResponseDTO;
import com.qoormthon.empty_wallet.global.exception.ErrorCode;
import com.qoormthon.empty_wallet.global.exception.InvalidValueException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 유저 관련 API 입니다.
 * 유저 정보를 조회하거나 목표 금액까지 남은 일 수를 계산하는 기능을 제공합니다.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserDocs {

  private final UserService userService;

  /**
   * 목표 금액까지 필요한 일 수를 계산합니다.
   * @param request 목표 금액, 월 수익, 월 고정지출 정보를 담은 요청 객체
   * @param bindingResult 요청 객체의 유효성 검증 결과
   * @return ResponseDTO<RequiredDaysResponse> 목표 금액까지 필요한 일 수를 담은 응답 객체
   */
  @Override
  @PostMapping("/required-days")
  public ResponseDTO<RequiredDaysResponse> calculateRequiredDays(@RequestBody @Valid RequiredDaysRequest request,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      log.error("입력 값이 비어있거나 올바르지 않습니다.");
      throw new InvalidValueException(ErrorCode.INVALID_INPUT_VALUE);
    }

    // 목표 금액까지 남은 일 수 입니다.
    double days = userService
        .getDaysToGoal(request.getTargetPrice(), request.getMonthlyPay());

    RequiredDaysResponse response = RequiredDaysResponse.builder()
        .days(days)
        .build();

    return ResponseDTO.of(response, "계산에 성공하였습니다.");
  }


}
