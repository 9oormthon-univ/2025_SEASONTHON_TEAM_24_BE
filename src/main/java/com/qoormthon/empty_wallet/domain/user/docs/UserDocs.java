package com.qoormthon.empty_wallet.domain.user.docs;

import com.qoormthon.empty_wallet.domain.user.dto.RequiredDaysRequest;
import com.qoormthon.empty_wallet.domain.user.dto.RequiredDaysResponse;
import com.qoormthon.empty_wallet.global.common.dto.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "남은 일수 계산 api / 유저 관련 api 입니다.", description = "유저가 입력한 값에 따라 목표 금액가지 남은 일수를 계산해줍니다.")
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
                        "localDateTime": "2025-09-01T23:26:53.599769",
                        "statusCode": 200,
                        "code": "SUCCESS",
                        "message": "계산에 성공하였습니다.",
                        "data": {
                          "days": 7.3
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
  @Operation(summary = "목표 금액까지 남은 일수 계산 api 입니다.", description = "엑세스 토큰은 필요하지 않습니다. / (monthlyPay(원 단위):월 수익), (monthlyCost(원 단위):월 고정지출), (targetPrice(원 단위):목표금액) 정보를 넣어주세요.")
  ResponseDTO<RequiredDaysResponse> calculateRequiredDays(RequiredDaysRequest request,
      BindingResult bindingResult);

}
