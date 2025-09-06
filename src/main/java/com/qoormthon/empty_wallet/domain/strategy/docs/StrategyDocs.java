package com.qoormthon.empty_wallet.domain.strategy.docs;

import com.qoormthon.empty_wallet.domain.strategy.dto.StrategyDataDTO;
import com.qoormthon.empty_wallet.global.common.dto.response.ListResponseDTO;
import com.qoormthon.empty_wallet.global.common.dto.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "전략 관련 api 입니다.", description = "전략 정보 조회 및 시작 기능을 제공합니다.")
public interface StrategyDocs {

  @ApiResponse(
      responseCode = "200",
      description = "전략 목록 조회 성공",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value =
                  """
                      {
                        "localDateTime": "2025-09-03T02:06:26.68314",
                        "statusCode": 200,
                        "code": "SUCCESS",
                        "message": "전략 목록 조회에 성공하였습니다.",
                        "data": {
                          "list": [
                            {
                              "type": "CAF",
                              "title": "집/사무실 드립 1잔으로 대체",
                              "description": "카페 대신 집이나 사무실에서 직접 드립 커피를 내려 마시기",
                              "dailySaving": 3700,
                              "howToStep": "1. 원두와 드리퍼 구비하기 2. 매일 아침 집에서 드립 커피 준비 3. 텀블러에 담아서 출근 4. 카페 방문 대신 준비한 커피 마시기",
                              "word": "기회비용: 어떤 선택을 할 때 포기해야 하는 다른 대안의 가치. 카페 커피를 포기하고 홈 드립을 선택함으로써 얻는 절약 효과"
                            },
                            {
                              "type": "CAF",
                              "title": "편의점 원두커피로 대체",
                              "description": "브랜드 카페 대신 편의점의 저렴한 원두커피로 변경",
                              "dailySaving": 3000,
                              "howToStep": "1. 평소 이용하던 카페 위치 파악 2. 근처 편의점 원두커피 기계 확인 3. 편의점 멤버십 가입으로 추가 할인 받기 4. 카페 방문 욕구가 들 때 편의점으로 이동",
                              "word": "대체재: 비슷한 기능을 하는 다른 상품. 브랜드 카페와 편의점 커피는 서로 대체재 관계"
                            }
                          ],
                          "totalCount": 2
                        }
                      }
                  """
          )
      )
  )
  @Operation(summary = "타입별 전략 조회 api 입니다.", description = "타입별로 페이징 처리된 전략을 조회합니다. / type: COM(제휴, 브랜드, 기업연계), PUB(정책)")
  public ResponseDTO<ListResponseDTO<List<StrategyDataDTO>>> getStrategiesByType(String type, int page, int size,
      HttpServletRequest httpServletRequest);


  @ApiResponse(
      responseCode = "200",
      description = "전략 목록 조회 성공",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value =
                  """
                      {
                        "localDateTime": "2025-09-05T22:20:16.092135",
                        "statusCode": 200,
                        "code": "SUCCESS",
                        "message": "전략 목록 조회에 성공하였습니다.",
                        "data": {
                          "totalCount": 10,
                          "list": [
                            {
                              "strategyId": 62,
                              "type": "SUB",
                              "title": "안 보는 OTT 하루 해지",
                              "description": "이용하지 않는 스트리밍 서비스를 즉시 해지하여 월 구독료 절약",
                              "dailySaving": 1000,
                              "monthlySaving": 30000,
                              "dailyReducedDays": -0.4,
                              "monthlyReducedDays": -11.9,
                              "howToStep": "1. 지난 한 달간 시청한 OTT 서비스 리스트 작성 2. 한 번도 이용하지 않은 서비스 확인 3. 해당 서비스 즉시 해지 처리 4. 필요 시 단발성 이용권으로 대체하기",
                              "word": "구독경제: 소유 대신 이용에 대해 정기적으로 비용을 지불하는 경제 모델. 사용하지 않는 서비스도 계속 과금되는 특징"
                            },
                            {
                              "strategyId": 71,
                              "type": "SUB",
                              "title": "중복 앱 결제 취소",
                              "description": "비슷한 기능의 여러 앱에 중복으로 결제하는 것 방지",
                              "dailySaving": 2000,
                              "monthlySaving": 60000,
                              "dailyReducedDays": -0.8,
                              "monthlyReducedDays": -22.7,
                              "howToStep": "1. 스마트폰에 설치된 유료 앱들 카테고리별 분류 2. 같은 기능을 하는 앱이 여러 개인지 확인 3. 가장 자주 사용하는 앱 하나만 남기고 구독 취소 4. 필요 시 무료 앱으로 대체 가능한지 검토",
                              "word": "기능 중복성: 여러 도구가 같은 목적을 수행하는 상황. 중복된 기능에 대한 중복 지출 방지가 중요"
                            }
                          ]
                        }
                      }
                  """
          )
      )
  )
  @Operation(summary = "유저에게 지정된 캐릭터의 전략 조회 api 입니다.", description = "타입별로 페이징 처리된 전략을 조회합니다. / 해당 api를 호출하려면 먼저 캐릭터가 지정되어야 합니다.(캐릭터 지정 api: /api/surveys/responses)")
  public ResponseDTO<ListResponseDTO<List<StrategyDataDTO>>> getStrategiesByUser(int page, int size,
      HttpServletRequest httpServletRequest);


  @ApiResponse(
      responseCode = "200",
      description = "전략 목록 조회 성공",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value =
                  """
                  """
          )
      )
  )
  @Operation(summary = "도전 시작하기 api 입니다.", description = "도전 시작하기 버튼을 누르면 호출되는 api 입니다.")
  public ResponseDTO<String> startStrategy(Long strategyId, HttpServletRequest httpServletRequest);
}
