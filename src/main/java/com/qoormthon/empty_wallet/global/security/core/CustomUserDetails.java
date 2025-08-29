package com.qoormthon.empty_wallet.global.security.core;

import com.qoormthon.empty_wallet.domain.user.entity.User;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 커스텀 사용자 인증 정보를 담는 클래스입니다.
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

  private final User user;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()));
  }

  public Long getId() {
    return user.getId();
  }

  @Override
  public String getPassword() {
    return ""; //소셜 로그인 사용중이므로 비밀번호는 빈 문자열로 설정합니다
  }

  @Override
  public String getUsername() {
    return user.getSocialEmail(); //사용자 식별자로 소셜 이메일을 사용합니다
  }
}
