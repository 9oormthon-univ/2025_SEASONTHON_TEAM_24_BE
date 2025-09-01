package com.qoormthon.empty_wallet.domain.auth.controller;

import com.qoormthon.empty_wallet.domain.auth.docs.AuthDocs;
import com.qoormthon.empty_wallet.domain.auth.service.AuthService;
import com.qoormthon.empty_wallet.global.common.dto.response.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 관련 API 입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthDocs {

  private final AuthService authService;

  /**
   * 엑세스 토큰을 발급합니다.
   * @param request 리프레시 토큰 추출을 위한 http 요청 객체 입니다.
   * @return ResponseDTO<String> 발급된 엑세스 토큰을 공통응답객체에 담아 반환합니다.
   */
  @Override
  @PostMapping("/access-token")
  public ResponseDTO<String> issueAccessToken(HttpServletRequest request) {
    String accessToken = authService.issueAccessToken(request);

    return ResponseDTO.of(accessToken, "엑세스 토큰 발급에 성공하였습니다.");
  }

}
