package com.qoormthon.empty_wallet.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 목표 금액 도달까지 필요한 일 수 계산 응답 DTO
 * class: UserController
 * API: /api/users/required-days
 */
@Getter
@Setter
@Builder
public class RequiredDaysResponse {

  //목표 금액까지 필요한 일 수
  private double days;

}
