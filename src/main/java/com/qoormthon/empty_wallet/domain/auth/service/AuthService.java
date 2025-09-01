package com.qoormthon.empty_wallet.domain.auth.service;

import com.qoormthon.empty_wallet.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final JwtTokenProvider jwtTokenProvider;

  public String issueAccessToken(HttpServletRequest request) {

    String refreshToken = jwtTokenProvider.extractToken(request);
    String newAccessToken = jwtTokenProvider.createAccessToken(refreshToken);

    return newAccessToken;

  }

}
