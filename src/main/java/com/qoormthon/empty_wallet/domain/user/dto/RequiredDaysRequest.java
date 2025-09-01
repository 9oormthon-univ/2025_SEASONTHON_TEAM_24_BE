package com.qoormthon.empty_wallet.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
  * 목표 금액 도달까지 필요한 일 수 계산 요청 DTO
  * class: UserController
  * API: /api/users/required-days
 */
@Getter
@Setter
public class RequiredDaysRequest {

  //월 수익
  @NotNull
  private Long monthlyPay;

  //월 고정지출
  @NotNull
  private Long monthlyCost;

  //목표금액
  @NotNull
  private Long targetPrice;

}
