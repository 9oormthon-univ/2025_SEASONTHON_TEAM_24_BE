package com.qoormthon.empty_wallet.global.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qoormthon.empty_wallet.domain.strategy.dto.StrategyDataDTO;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StrategyDataConfig {

  private final ResourceLoader resourceLoader;
  private final ObjectMapper objectMapper;

  @Bean
  public List<StrategyDataDTO> loadStrategyData() {
    try {
      Resource resource = resourceLoader.getResource("classpath:static/data/strategyData.json");
      InputStream inputStream = resource.getInputStream();
      List<StrategyDataDTO> strategies = objectMapper.readValue(inputStream,
          new TypeReference<List<StrategyDataDTO>>(){});
      log.info(String.format("Loaded %d strategies", strategies.size()));
      return strategies;
    } catch (Exception e) {
      log.error("파싱 중 에러 발생 :" + e.getMessage());
      return List.of();
    }
  }

}
