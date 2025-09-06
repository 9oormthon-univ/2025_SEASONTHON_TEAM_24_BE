package com.qoormthon.empty_wallet.domain.strategy.entity;

import com.qoormthon.empty_wallet.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "strategy_active")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StrategyActive {
  @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long strategyActiveId;

  //연관관계 매핑 안함
  private Long strategyId;

  @JoinColumn(name = "user_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(length = 255)
  private StrategyStatus status;

  @Enumerated(EnumType.STRING)
  private StrategyType type;

  private Long count = 0L;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  @Builder
  private StrategyActive(Long strategyActiveId, Long strategyId, User user, StrategyStatus status, StrategyType type) {
    this.strategyActiveId = strategyActiveId;
    this.strategyId = strategyId;
    this.user = user;
    this.status = status;
    this.type = type;
  }

  public static StrategyActive of(Long strategyId, User user, StrategyStatus status, StrategyType type) {
    return StrategyActive.builder()
        .strategyActiveId(null)
        .strategyId(strategyId)
        .user(user)
        .status(status)
        .type(type)
        .build();
  }

}
