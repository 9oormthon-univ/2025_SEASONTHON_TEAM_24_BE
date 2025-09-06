package com.qoormthon.empty_wallet.domain.strategy.repository;

import com.qoormthon.empty_wallet.domain.strategy.entity.StrategyActive;
import com.qoormthon.empty_wallet.domain.strategy.entity.StrategyStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StrategyActiveRepository extends JpaRepository<StrategyActive, Long> {

  List<StrategyActive> findByStatus(StrategyStatus status);
  Optional<StrategyActive> findByStrategyId(Long strategyId);


}
