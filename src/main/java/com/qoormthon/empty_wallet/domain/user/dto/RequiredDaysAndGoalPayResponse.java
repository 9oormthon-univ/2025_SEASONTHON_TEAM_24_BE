package com.qoormthon.empty_wallet.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequiredDaysAndGoalPayResponse {

  //목표 금액까지 필요한 일 수
  private double days;
  private Long targetPrice;

  public static RequiredDaysAndGoalPayResponse of(double days, Long targetPrice) {
    return RequiredDaysAndGoalPayResponse.builder()
        .days(days)
        .targetPrice(targetPrice)
        .build();
  }

}
