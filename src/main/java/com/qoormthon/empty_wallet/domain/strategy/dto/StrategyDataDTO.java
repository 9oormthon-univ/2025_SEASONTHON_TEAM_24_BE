package com.qoormthon.empty_wallet.domain.strategy.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StrategyDataDTO {

  private String type;
  private String title;
  private String description;
  private Integer dailySaving;  // Long -> Integer 변경
  private String howToStep;
  private String word;

}