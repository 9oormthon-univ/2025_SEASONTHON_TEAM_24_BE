package com.qoormthon.empty_wallet.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {

  private String name;
  private Long monthlyPay;
  private Long targetPrice;
  private double days;
}
