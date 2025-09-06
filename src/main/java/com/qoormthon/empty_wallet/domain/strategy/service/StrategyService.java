package com.qoormthon.empty_wallet.domain.strategy.service;

import com.qoormthon.empty_wallet.domain.character.entity.Character;
import com.qoormthon.empty_wallet.domain.strategy.dto.StrategyDataDTO;
import com.qoormthon.empty_wallet.domain.strategy.entity.StrategyActive;
import com.qoormthon.empty_wallet.domain.strategy.entity.StrategyStatus;
import com.qoormthon.empty_wallet.domain.strategy.entity.StrategyType;
import com.qoormthon.empty_wallet.domain.strategy.repository.StrategyActiveRepository;
import com.qoormthon.empty_wallet.domain.user.entity.User;
import com.qoormthon.empty_wallet.domain.user.repository.UserRepository;
import com.qoormthon.empty_wallet.global.exception.ErrorCode;
import com.qoormthon.empty_wallet.global.exception.InternalServerException;
import com.qoormthon.empty_wallet.global.exception.InvalidValueException;
import com.qoormthon.empty_wallet.global.exception.NotFoundInfoException;
import com.qoormthon.empty_wallet.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StrategyService {

  private final List<StrategyDataDTO> strategies;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;
  private final StrategyActiveRepository strategyActiveRepository;
  private final AbstractJackson2HttpMessageConverter abstractJackson2HttpMessageConverter;

  @Transactional
  public List<StrategyDataDTO> getStrategiesByType(String type, int page, int size, HttpServletRequest httpServletRequest) {
    try {
      String accessToken = jwtTokenProvider.extractToken(httpServletRequest);
      Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
      User user = userRepository.findById(userId).orElse(null);

      // 유저가 존재하지 않을 경우 예외 처리
      if(user == null) {
        log.error("존재하지 않는 유저입니다.");
        throw new NotFoundInfoException(ErrorCode.USER_NOT_FOUND);
      }

      if(user.getMonthlyPay() == null) {
        log.error("유저의 월 수익 정보가 없습니다.");
        throw new InvalidValueException(ErrorCode.INVALID_INPUT_VALUE);
      }


      List<StrategyDataDTO> filteredStrategies = filteredStrategies(type);

      int start = page * size;
      int end = Math.min((start + size), filteredStrategies.size());

      // 한 달 실천 절약액
      for(StrategyDataDTO strategy : filteredStrategies) {
        strategy.setMonthlySaving(strategy.getDailySaving()*30);
      }

      // 하루/한달 실천 시 차감 일 수
      for(StrategyDataDTO strategy : filteredStrategies) {
        double monthlySavingMoney = user.getMonthlyPay()/10.0;
        Long targetPrice = user.getTargetPrice();
        Integer dailyIncrease = strategy.getDailySaving();

        double dailyReducedDays = Math.round(calculateDailyReducedDays(monthlySavingMoney, targetPrice, dailyIncrease) * 10.0) / 10.0;
        double monthlyReducedDays = Math.round(calculateMonthlyReducedDays(monthlySavingMoney, targetPrice, dailyIncrease) * 10.0) / 10.0;

        strategy.setDailyReducedDays(dailyReducedDays);
        strategy.setMonthlyReducedDays(monthlyReducedDays);
      }

      // 전략 상태 설정
      for(StrategyDataDTO strategy : filteredStrategies) {

        StrategyActive strategyActive =  strategyActiveRepository.findByStrategyId(strategy.getStrategyId()).orElse(null);

        // 해당 전략을 '도전 시작하기' 누르지 않은 경우
        if(strategyActive == null) {
          strategy.setStatus(StrategyStatus.WAITING);
        }

        // 해당 전략을 완료하였을 경우
        else if(strategyActive.getStatus().equals(StrategyStatus.DONE)) {
          strategy.setStatus(StrategyStatus.DONE);
        }

        else if(strategyActive.getStatus().equals(StrategyStatus.RUNNING)) {
          strategy.setStatus(StrategyStatus.RUNNING);
        }
      }
      return filteredStrategies.subList(start, end);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  @Transactional
  public List<StrategyDataDTO> getStrategiesByrunning(HttpServletRequest httpServletRequest) {

    try {
      String accessToken = jwtTokenProvider.extractToken(httpServletRequest);
      Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
      User user = userRepository.findById(userId).orElse(null);

      if(user == null) {
        log.error("사용자 정보가 존재하지 않습니다.");
        throw new NotFoundInfoException(ErrorCode.USER_NOT_FOUND);
      }

      List<StrategyDataDTO> response = new ArrayList<>();
      List<StrategyActive> strategyActives = strategyActiveRepository.findByStatus(StrategyStatus.RUNNING);


      // status 지정 (running)
      for(StrategyActive strategyActive : strategyActives) {
        StrategyDataDTO strategyDataDTO = filteredStrategies(strategyActive.getStrategyId());
        strategyDataDTO.setStatus(StrategyStatus.RUNNING);
        response.add(strategyDataDTO);
      }

      // 한 달 실천 절약액
      for(StrategyDataDTO strategy : response) {
        strategy.setMonthlySaving(strategy.getDailySaving()*30);
      }

      // 데일리/위클리 상태값 설정
      for(StrategyDataDTO strategy : response) {
        StrategyActive strategyActive = strategyActiveRepository.findByStrategyId(strategy.getStrategyId()).orElse(null);

        if(strategyActive == null) {
          strategy.setGoalType(StrategyType.DAILY);
        } else {
          strategy.setGoalType(strategyActive.getType());
        }
      }

      // 하루/한달 실천 시 차감 일 수
      for(StrategyDataDTO strategy : response) {
        double monthlySavingMoney = user.getMonthlyPay()/10.0;
        Long targetPrice = user.getTargetPrice();
        Integer dailyIncrease = strategy.getDailySaving();

        double dailyReducedDays = Math.round(calculateDailyReducedDays(monthlySavingMoney, targetPrice, dailyIncrease) * 10.0) / 10.0;
        double monthlyReducedDays = Math.round(calculateMonthlyReducedDays(monthlySavingMoney, targetPrice, dailyIncrease) * 10.0) / 10.0;

        strategy.setDailyReducedDays(dailyReducedDays);
        strategy.setMonthlyReducedDays(monthlyReducedDays);
      }



      return response;


    } catch (Exception e) {
      log.error(e.getMessage());
      throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

  }

  @Transactional
  public List<StrategyDataDTO> getStrategiesByUser(int page, int size, HttpServletRequest httpServletRequest) {

    try {
      String accessToken = jwtTokenProvider.extractToken(httpServletRequest);
      Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
      int start = page * size;
      int end = 0;

      // 유저가 존재하지 않을 경우 예외 처리
      if(userRepository.findById(userId).isEmpty()) {
        log.error("존재하지 않는 유저입니다.");
        throw new NotFoundInfoException(ErrorCode.USER_NOT_FOUND);
      }

      User user = userRepository.findById(userId).orElse(null);

      Character character = user.getCharacter();

      // 유저에게 캐릭터가 지정되지 않은 경우 예외 처리
      if(character == null) {
        log.error("유저에게 지정된 캐릭터가 없습니다.");
        throw new NotFoundInfoException(ErrorCode.NOT_FOUND);
      }

      if(user.getMonthlyPay() == null) {
        log.error("유저의 월 수익 정보가 없습니다.");
        throw new InvalidValueException(ErrorCode.INVALID_INPUT_VALUE);
      }

      // 캐릭터 타입 조회(ex.CAF..)
      String type = character.getCode();

      // 캐릭터 타입 별 전략 필터링
      List<StrategyDataDTO> filteredStrategies = filteredStrategies(type);

      end = Math.min((start + size), filteredStrategies.size());


      // 한 달 실천 절약액
      for(StrategyDataDTO strategy : filteredStrategies) {
        strategy.setMonthlySaving(strategy.getDailySaving()*30);
      }

      // 데일리/위클리 상태값 설정
      for(StrategyDataDTO strategy : filteredStrategies) {
        StrategyActive strategyActive = strategyActiveRepository.findByStrategyId(strategy.getStrategyId()).orElse(null);

        if(strategyActive == null) {
          strategy.setGoalType(StrategyType.DAILY);
        } else {
          strategy.setGoalType(strategyActive.getType());
        }
      }

      // 전략 상태 설정
      for(StrategyDataDTO strategy : filteredStrategies) {

        StrategyActive strategyActive = strategyActiveRepository.findByStrategyId(strategy.getStrategyId()).orElse(null);

        // 해당 전략을 '도전 시작하기' 누르지 않은 경우
        if(strategyActive == null) {
          strategy.setStatus(StrategyStatus.WAITING);
        }

        // 해당 전략을 완료하였을 경우
        else if(strategyActive.getStatus().equals(StrategyStatus.DONE)) {
          strategy.setStatus(StrategyStatus.DONE);
        }

        else {
          strategy.setStatus(StrategyStatus.RUNNING);
        }
      }

      // 하루/한달 실천 시 차감 일 수
      for(StrategyDataDTO strategy : filteredStrategies) {
        double monthlySavingMoney = user.getMonthlyPay()/10.0;
        Long targetPrice = user.getTargetPrice();
        Integer dailyIncrease = strategy.getDailySaving();

        double dailyReducedDays = Math.round(calculateDailyReducedDays(monthlySavingMoney, targetPrice, dailyIncrease) * 10.0) / 10.0;
        double monthlyReducedDays = Math.round(calculateMonthlyReducedDays(monthlySavingMoney, targetPrice, dailyIncrease) * 10.0) / 10.0;

        strategy.setDailyReducedDays(dailyReducedDays);
        strategy.setMonthlyReducedDays(monthlyReducedDays);
      }

      //페이징 처리된 전략 반환
      return filteredStrategies.subList(start, end);


    } catch (Exception e) {
      log.error(e.getMessage());
      throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  @Transactional
  public void startStrategy(Long strategyId, HttpServletRequest httpServletRequest) {
    String token = jwtTokenProvider.extractToken(httpServletRequest);
    Long userId = jwtTokenProvider.getUserIdFromToken(token);
    User user = userRepository.findById(userId).orElse(null);

    if(user == null) {
      log.error("존재하지 않는 유저 입니다.");
      throw new NotFoundInfoException(ErrorCode.USER_NOT_FOUND);
    }

    if(filteredStrategies(strategyId) == null) {
      log.error("존재하지 않는 전략 입니다.");
      throw new InvalidValueException(ErrorCode.INVALID_INPUT_VALUE);
    }

    StrategyActive strategyActive = StrategyActive.of(strategyId, user, StrategyStatus.RUNNING, StrategyType.DAILY);

    strategyActiveRepository.save(strategyActive);
  }



  // type별 전략 반환
  public List<StrategyDataDTO> filteredStrategies(String type) {
    List<StrategyDataDTO> filteredStrategies = strategies.stream()
        .filter(strategy -> strategy.getType().equals(type.toUpperCase()))
        .toList();

    return filteredStrategies;
  }

  public StrategyDataDTO filteredStrategies(Long id) {
    List<StrategyDataDTO> filteredStrategies = strategies.stream()
        .filter(strategy -> strategy.getStrategyId().equals(id))
        .toList();
    return filteredStrategies.get(0);
  }

  public double calculateMonthlyReducedDays(double monthlySavingMoney, Long targetPrice, Integer dailyIncrease) {
    // 기존: 한달 저축액으로 목표 달성까지 걸리는 일수
    double originalDays = ((double)targetPrice / monthlySavingMoney) * 30;

    // 하루 저축액 증가 적용한 새로운 한달 저축액
    double newMonthlySavings = monthlySavingMoney + (dailyIncrease * 30);

    // 새로운 목표 달성까지 걸리는 일수
    double newDays = (targetPrice / newMonthlySavings) * 30;

    // 단축된 일수
    return newDays - originalDays;
  }

  public double calculateDailyReducedDays(double monthlySavingMoney, Long targetPrice, Integer dailyIncrease) {
    // 기존: 한달 저축액으로 목표 달성까지 걸리는 일수
    double originalDays = ((double)targetPrice / monthlySavingMoney) * 30;

    // 하루 저축액 증가 적용한 새로운 한달 저축액
    double newMonthlySavings = monthlySavingMoney + dailyIncrease;

    // 새로운 목표 달성까지 걸리는 일수
    double newDays = (targetPrice / newMonthlySavings) * 30;

    // 단축된 일수
    return newDays - originalDays;
  }



}
