package com.qoormthon.empty_wallet.domain.auth.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "카카오 소셜로그인", description = "OAuth2 인증")
public class OAuth2DocsController {

  @Operation(
      summary = "카카오 소셜로그인",
      description = "카카오 소셜 로그인 페이지로 리다이렉트됩니다.",
      responses = {
          @ApiResponse(responseCode = "302", description = "카카오 로그인 페이지로 리다이렉트")
      }
  )
  @GetMapping("/oauth2/authorization/kakao")
  public void socialLogin() {
    // 문서화 목적의 컨트롤러 입니다.
  }

}
