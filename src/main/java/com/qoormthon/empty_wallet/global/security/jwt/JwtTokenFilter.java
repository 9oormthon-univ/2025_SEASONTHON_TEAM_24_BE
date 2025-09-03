package com.qoormthon.empty_wallet.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qoormthon.empty_wallet.domain.user.exception.UserNotFoundException;
import com.qoormthon.empty_wallet.global.common.dto.response.ResponseDTO;
import com.qoormthon.empty_wallet.global.common.threadlocal.TraceIdHolder;
import com.qoormthon.empty_wallet.global.exception.ErrorCode;
import com.qoormthon.empty_wallet.global.security.core.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * JWT 토큰을 검사하고 사용자 인증 정보를 설정하는 필터 클래스입니다. 이 필터는 모든 HTTP 요청을 가로채어 JWT 토큰의 유효성을 검사하고 유효한 경우 사용자 인증
 * 정보를 SecurityContext에 저장합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

  /**
   * Jwt 토큰 생성 및 검증을 담당하는 객체입니다.
   */
  private final JwtTokenProvider jwtTokenProvider;

  /**
   * 사용자 인증 정보 생성을 담당하는 서비스 객체입니다.
   */
  private final CustomUserDetailsService customUserDetailsService;

  /**
   * 예외처리 응답을 위해 사용되는 객체입니다.
   */
  private final ObjectMapper objectMapper;


  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    try {
      // TraceID 발급
      TraceIdHolder.set(UUID.randomUUID().toString().substring(0, 8));

      String url = request.getRequestURI();
      String method = request.getMethod();
      String accessToken = this.getTokenFromRequest(request);

      // 토큰이 있고 유효하면 인증 세팅 시도
      if (StringUtils.hasText(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
        try {
          UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(accessToken);
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          log.info("[{}][{}] {} {} (auth set)", TraceIdHolder.get(), request.getRemoteAddr(), method, url);
        } catch (UserNotFoundException e) {
          // 유저가 없으면 응답 쓰지 않고 '비인증' 상태로 계속 진행 (공개 API 깨지지 않도록)
          log.warn("[{}] user not found for token, proceed unauthenticated. uri={}",
                  TraceIdHolder.get(), url);
        }
      } else {
        // 토큰 없거나 무효 → 비인증 상태로 진행
        log.info("[{}][{}] {} {} (no/invalid token)", TraceIdHolder.get(), request.getRemoteAddr(), method, url);
      }

      filterChain.doFilter(request, response);
    } finally {
      TraceIdHolder.clear();
    }
  }



  /**
   * 요청 헤더에서 JWT 토큰을 추출하는 메서드입니다.
   *
   * @param request HTTP 요청 객체
   * @return 요청 헤더에서 추출한 순수 JWT 토큰 문자열을 반환합니다
   */
  private String getTokenFromRequest(HttpServletRequest request) {

    // Authorization 헤더에서 JWT 토큰을 추출합니다.
    String token = request.getHeader("Authorization");

    // 토큰이 "Bearer "로 시작하는지 확인합니다.
    if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {

      //Bearer 제거하고 토큰만을 추출합니다
      token = token.substring(7);
    }

    return token;
  }

  private UsernamePasswordAuthenticationToken getAuthentication(String token) {

    //토큰에서 사용자 ID를 추출합니다.
    Long userId = jwtTokenProvider.getUserIdFromToken(token);

    //사용자 ID로 사용자 인증 정보를 생성합니다.
    UserDetails userDetails = customUserDetailsService.loadUserById(userId);

    return new UsernamePasswordAuthenticationToken(
        userDetails,
        null,
        userDetails.getAuthorities()
    );
  }

}
