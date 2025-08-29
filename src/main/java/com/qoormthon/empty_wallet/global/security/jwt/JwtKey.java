package com.qoormthon.empty_wallet.global.security.jwt;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JWT 서명에 사용할 비밀키를 설정하는 클래스
 * <p>
 * HmacSHA512 암호화 알고리즘을 사용합니다.
 */
@Configuration
public class JwtKey {

  /**
   * application.properties에서 JWT 비밀키를 주입받습니다.
   */
  @Value("${jwt.secretKey}")
  private String secretKey;

  /**
   * 서명키를 만들어서 반환하는 메서드.
   *
   * @return SecretKey 객체
   */
  @Bean
  public SecretKey secretKey() {
    byte[] keyBytes = secretKey.getBytes();
    return new SecretKeySpec(keyBytes, "HmacSHA512");
  }
}
