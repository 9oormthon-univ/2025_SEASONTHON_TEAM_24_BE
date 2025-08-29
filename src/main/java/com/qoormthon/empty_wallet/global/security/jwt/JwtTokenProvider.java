package com.qoormthon.empty_wallet.global.security.jwt;

import com.qoormthon.empty_wallet.global.common.threadlocal.TraceIdHolder;
import com.qoormthon.empty_wallet.global.security.core.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * JWT 토큰 생성 및 추출, 검증하는 클래스 입니다.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final SecretKey secretKey;

  @Value("${jwt.accessTokenExpirationMs}")
  private Long accessTokenExpirationTime;

  @Value("${jwt.refreshTokenExpirationMs}")
  private Long refreshTokenExpirationTime;

  /**
   * JWT 토큰을 생성하는 메서드입니다.
   *
   * @param authentication   인증 정보
   * @param expirationMillis 토큰 만료 시간 (밀리초 단위)
   * @return 생성된 JWT 토큰
   */
  public String createToken(Authentication authentication, Long expirationMillis) {
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
    Date expiryDate = new Date(new Date().getTime() + expirationMillis);

    return Jwts.builder()
        .subject(customUserDetails.getUsername())
        .claim("userId", customUserDetails.getId())
        .issuedAt(new Date())
        .expiration(expiryDate)
        .signWith(secretKey, SignatureAlgorithm.HS512)
        .compact();
  }

  /**
   * 액세스 토큰을 생성하는 메서드입니다.
   *
   * @param authentication 인증 정보
   * @return 생성된 JWT 토큰
   */
  public String createAccessToken(Authentication authentication) {
    return createToken(authentication, accessTokenExpirationTime);
  }

  /**
   * 리프레시 토큰을 생성하는 메서드입니다.
   *
   * @param authentication 인증 정보
   * @return 생성된 JWT 토큰
   */
  public String createRefreshToken(Authentication authentication) {
    return createToken(authentication, refreshTokenExpirationTime);
  }

  /**
   * JWT 토큰에서 사용자 ID를 추출하는 메서드입니다.
   *
   * @param token JWT 토큰
   * @return 사용자 ID
   */
  public Long getUserIdFromToken(String token) {
    return Jwts
        .parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("userId", Long.class);
  }


  /**
   * JWT 토큰의 유효성을 검증하는 메서드입니다.
   *
   * @param token JWT 토큰
   * @return 유효성 검사 결과 (true: 유효, false: 무효)
   */
  public Boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token);
      return true;

    } catch (MalformedJwtException e) {
      //토큰 형식이 잘못됨
      log.error("[" + TraceIdHolder.get() + "]:(토큰 형식이 잘못됨)");
      return false;

    } catch (ExpiredJwtException e) {
      //토큰이 만료됨
      log.error("[" + TraceIdHolder.get() + "]:(토큰이 만료됨)");
      return false;

    } catch (IllegalArgumentException e) {
      //토큰이 비어있거나 잘못된 형식
      log.error("[" + TraceIdHolder.get() + "]:(토큰이 비어있거나 잘못된 형식)");
      return false;

    } catch (SignatureException e) {
      //시그니처 검증 실패
      log.error("[" + TraceIdHolder.get() + "]:(시그니처 검증 실패)");
      return false;

    } catch (JwtException e) {
      //기타 JWT 관련 예외
      log.error("[" + TraceIdHolder.get() + "]:(기타 JWT 예외 발생)");
      return false;
    }

  }

}
