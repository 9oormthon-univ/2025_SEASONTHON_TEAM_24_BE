package com.qoormthon.empty_wallet.domain.strategy.docs;

import com.qoormthon.empty_wallet.domain.strategy.dto.StrategyDataDTO;
import com.qoormthon.empty_wallet.global.common.dto.response.ListResponseDTO;
import com.qoormthon.empty_wallet.global.common.dto.response.ResponseDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "타입별 전략 조회 api 입니다.", description = "타입별로 페이징 처리된 전략을 조회합니다. / type: COM(제휴, 브랜드, 기업연계), PUB(정책)")
public interface StrategyDocs {

  @ApiResponse(
      responseCode = "200",
      description = "전략 목록 조회 성공 / ",
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
  public ResponseDTO<ListResponseDTO<List<StrategyDataDTO>>> getStrategiesByType(String type, int page, int size);
}
