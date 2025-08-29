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
      HttpServletResponse response, FilterChain filterChain
  ) throws ServletException, IOException {

    try {
      //TraceID 발급 및 설정
      TraceIdHolder.set(UUID.randomUUID().toString().substring(0, 8));

      //요청 헤더에서 JWT 토큰을 추출합니다.
      String accessToken = this.getTokenFromRequest(request);

      //로깅을 위해 요청 URL을 가져옵니다.
      String url = request.getRequestURI().toString();

      //로깅을 위해 요청 메서드를 가져옵니다.
      String method = request.getMethod();

      //토큰이 존재하고 유효한 경우를 확인합니다.
      if (jwtTokenProvider.validateToken(accessToken) && accessToken != null) {
        log.info(
            "[" + TraceIdHolder.get() + "]" + "[" + request.getRemoteAddr() + "]:" + "[" + method
                + ":" + url + "]" + "(allowed)");

        //토큰에서 사용자 조회 후 인증 객체를 생성합니다.(유저가 존재하지 않을 경우 CustomUserNotFoundException 발생)
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(accessToken);

        //인증된 사용자 객체를 시큐리티 컨텍스트에 설정합니다.
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      } else {
        log.info(
            "[" + TraceIdHolder.get() + "]" + "[" + request.getRemoteAddr() + "]:" + "[" + method
                + ":" + url + "]" + "(denied)");
      }

      filterChain.doFilter(request, response);

    } catch (UserNotFoundException e) {
      log.info(
          "[" + TraceIdHolder.get() + "]" + "(해당 사용자를 찾을 수 없습니다.)");

      // 유저가 존재하지 않을 경우 반환되는 json 응답.
      String userNotFoundExceptionResponse = objectMapper
          .writeValueAsString(ResponseDTO.of(ErrorCode.USER_NOT_FOUND));
      response.setStatus(ErrorCode.USER_NOT_FOUND.getHttpStatus().value());
      response.setContentType("application/json");
      response.getWriter().write(userNotFoundExceptionResponse);
      
    } finally {
      // 요청이 끝난 후 TraceIdHolder를 정리합니다.
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
