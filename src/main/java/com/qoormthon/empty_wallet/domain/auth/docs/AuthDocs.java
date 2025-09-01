package com.qoormthon.empty_wallet.domain.auth.docs;

import com.qoormthon.empty_wallet.global.common.dto.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
@Tag(name = "인증 관련 api 입니다.", description = "")
public interface AuthDocs {


  @ApiResponse(
      responseCode = "200",
      description = "발급 성공",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value =
                  """
                      {
                          "localDateTime": "2025-09-01T12:52:07.96523",
                          "statusCode": 200,
                          "code": "SUCCESS",
                          "message": "엑세스 토큰 발급에 성공하였습니다.",
                          "data": "{accessToken}"
                      }
                  """
          )
      )
  )
  @ApiResponse(
      responseCode = "401",
      description = "토큰 추출 실패 / 헤더에 리프레시 토큰이 없거나 형식이 잘못된 경우",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value =
                  """
                      {
                          "localDateTime": "2025-09-01T12:57:00.608459",
                          "statusCode": 401,
                          "code": "TOKEN_EXTRACTION_FAILED",
                          "message": "토큰 추출에 실패했습니다.",
                          "data": null
                      }
                  """
          )
      )
  )
  @Operation(summary = "엑세스 토큰 발급 api입니다.", description = "헤더에 리프레시 토큰을 넣어주세요. / Authorization: Bearer {refreshToken}")
  ResponseDTO<String> issueAccessToken(HttpServletRequest request);

}
