package com.qoormthon.empty_wallet.domain.strategy.service;

import com.qoormthon.empty_wallet.domain.strategy.dto.StrategyDataDTO;
import com.qoormthon.empty_wallet.global.exception.ErrorCode;
import com.qoormthon.empty_wallet.global.exception.InternalServerException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StrategyService {

  private final List<StrategyDataDTO> strategies;

  public List<StrategyDataDTO> getStrategiesByType(String type, int page, int size) {
    try {
      List<StrategyDataDTO> filteredStrategies = strategies.stream()
          .filter(strategy -> strategy.getType().equals(type.toUpperCase()))
          .toList();

      int start = page * size;
      int end = Math.min((start + size), filteredStrategies.size());

      // 한 달 실천 절약액
      for(StrategyDataDTO strategy : filteredStrategies) {
        strategy.setMonthlySaving(strategy.getDailySaving()*30);
      }


      return filteredStrategies.subList(start, end);
    } catch (Exception e) {
      throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

}
