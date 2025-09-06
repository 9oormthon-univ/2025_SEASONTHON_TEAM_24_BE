package com.qoormthon.empty_wallet.domain.strategy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qoormthon.empty_wallet.domain.strategy.entity.StrategyStatus;
import com.qoormthon.empty_wallet.domain.strategy.entity.StrategyType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StrategyDataDTO {

  private Long strategyId;
  private String type;
  private String title;
  private String description;
  private Integer dailySaving;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private StrategyType goalType;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Integer monthlySaving;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Double dailyReducedDays;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Double monthlyReducedDays;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private StrategyStatus status;


  private List<HowToStep> howToStep;
  private Word word;

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  public static class HowToStep {
    private String title;
    private String content;
  }


  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  public static class Word {
    private String keyword;
    private String meaning;
  }

}