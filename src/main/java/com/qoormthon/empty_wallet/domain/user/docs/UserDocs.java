package com.qoormthon.empty_wallet.domain.user.docs;

import com.qoormthon.empty_wallet.domain.user.dto.RequiredDaysAndGoalPayResponse;
import com.qoormthon.empty_wallet.domain.user.dto.RequiredDaysRequest;
import com.qoormthon.empty_wallet.domain.user.dto.RequiredDaysResponse;
import com.qoormthon.empty_wallet.global.common.dto.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "유저 관련 api 입니다.", description = "유저가 입력한 값에 따라 목표 금액까지 남은 일수를 계산해줍니다.")
public interface UserDocs {

  @ApiResponse(
      responseCode = "200",
      description = "계산 성공",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value =
                  """
                      {
                        "localDateTime": "2025-09-05T23:08:34.810011023",
                        "statusCode": 200,
                        "code": "SUCCESS",
                        "message": "계산에 성공하였습니다.",
                        "data": {
                          "days": 300
                        }
                      }
                  """
          )
      )
  )
  @ApiResponse(
      responseCode = "400",
      description = "계산 값이 음수 이거나 무한대, null 인 경우",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value =
                  """
                      {
                        "localDateTime": "2025-09-01T23:27:43.440503",
                        "statusCode": 400,
                        "code": "INVALID_INPUT_VALUE",
                        "message": "입력값이 유효하지 않습니다.",
                        "data": null
                      }
                  """
          )
      )
  )
  @Operation(summary = "목표 금액까지 남은 일수 계산 api 입니다.", description = "monthlyPay:월 수익(원), targetPrice:목표금액(원) 정보를 넣어주세요.")
  ResponseDTO<RequiredDaysResponse> calculateRequiredDays(RequiredDaysRequest request,
      BindingResult bindingResult, HttpServletRequest httpServletRequest);



  @ApiResponse(
      responseCode = "200",
      description = "삭제 성공",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value =
                  """
                      {
                        "localDateTime": "2025-09-05T23:08:34.810011023",
                        "statusCode": 200,
                        "code": "SUCCESS",
                        "message": "삭제에 성공하였습니다!",
                        "data": null
                      }
                  """
          )
      )
  )
  @ApiResponse(
      responseCode = "400",
      description = "계산 값이 음수 이거나 무한대, null 인 경우",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value =
                  """
                      {
                        "localDateTime": "2025-09-01T23:27:43.440503",
                        "statusCode": 400,
                        "code": "INVALID_INPUT_VALUE",
                        "message": "입력값이 유효하지 않습니다.",
                        "data": null
                      }
                  """
          )
      )
  )
  @Operation(summary = "사용자 전체 삭제 api 입니다.", description = "db에 로그인된 사용자를 모두 제거합니다.")
  public ResponseDTO<String> deleteAllUser();



  @ApiResponse(
      responseCode = "200",
      description = "조회에 성공한 경우",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value =
                  """
                      {
                        "localDateTime": "2025-09-06T22:19:13.892748",
                        "statusCode": 200,
                        "code": "SUCCESS",
                        "message": "조회에 성공하였습니다.",
                        "data": {
                          "days": 30000,
                          "targetPrice": 100000
                        }
                      }
                  """
          )
      )
  )
  @Operation(summary = "목표금액, 목표 금액까지 남은 기간을 반환하는 api 입니다.", description = "토큰이 필요하며 먼저 POST:/api/users/required-days 로 목표 금액과 월 수익을 입력해야 합니다.")
  public ResponseDTO<RequiredDaysAndGoalPayResponse> calculateRequiredDaysAndGoalpay(HttpServletRequest httpServletRequest);


  @ApiResponse(
      responseCode = "200",
      description = "조회에 성공한 경우",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value =
                  """
                      {
                        "localDateTime": "2025-09-07T05:34:27.428159",
                        "statusCode": 200,
                        "code": "SUCCESS",
                        "message": "조회에 성공하였습니다.",
                        "data": {
                          "name": "홍준표",
                          "monthlyPay": 100,
                          "targetPrice": 1000,
                          "days": 3000
                        }
                      }
                  """
          )
      )
  )
  @Operation(summary = "로그인한 유저 정보를 조회하는 api 입니다.", description = "토큰이 필요하며 유저 이름을 반환합니다.")
  public ResponseDTO<?> getUserInfo(HttpServletRequest httpServletRequest);
}
