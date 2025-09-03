package com.qoormthon.empty_wallet.domain.strategy.controller;

import com.qoormthon.empty_wallet.domain.strategy.docs.StrategyDocs;
import com.qoormthon.empty_wallet.domain.strategy.dto.StrategyDataDTO;
import com.qoormthon.empty_wallet.domain.strategy.service.StrategyService;
import com.qoormthon.empty_wallet.global.common.dto.response.ListResponseDTO;
import com.qoormthon.empty_wallet.global.common.dto.response.ResponseDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 전략 관련 API 입니다.
 * 타입별 전략 정보를 조회하는 기능을 제공합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/strategies")
public class StrategyController implements StrategyDocs {

  private final StrategyService strategyService;

  @Override
  @GetMapping
  public ResponseDTO<ListResponseDTO<List<StrategyDataDTO>>> getStrategiesByType(
      @RequestParam String type,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    List<StrategyDataDTO> strategies = strategyService.getStrategiesByType(type, page, size);

    return ResponseDTO.of(ListResponseDTO.of(strategies), "전략 목록 조회에 성공하였습니다.");

  }






}
