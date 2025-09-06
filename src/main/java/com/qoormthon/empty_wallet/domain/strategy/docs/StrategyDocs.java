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
                        "localDateTime": "2025-09-07T04:08:28.47472908",
                        "statusCode": 200,
                        "code": "SUCCESS",
                        "message": "전략 목록 조회에 성공하였습니다.",
                        "data": {
                          "totalCount": 10,
                          "list": [
                            {
                              "strategyId": 1,
                              "type": "CAF",
                              "title": "집/사무실 드립 1잔으로 대체",
                              "description": "카페 대신 집이나 사무실에서 직접 드립 커피를 내려 마시기",
                              "dailySaving": 3700,
                              "goalType": "DAILY",
                              "monthlySaving": 111000,
                              "dailyReducedDays": -2991.9,
                              "monthlyReducedDays": -2999.7,
                              "status": "DONE",
                              "howToStep": [
                                {
                                  "title": "원두와 드리퍼 구비하고 가격 차이 확인하기",
                                  "content": "카페 커피와 드립 커피의 가격 차이를 기록해 두면, 어느새 '오늘은 집에서 내려 마셔야겠다'라는 생각이 더 자주 들 거예요."
                                },
                                {
                                  "title": "매일 아침 홈 카페 루틴 만들기",
                                  "content": "아침 10분 일찍 일어나서 드립 커피를 내리는 루틴을 만들어보세요. 여유로운 시작과 함께 하루 3,700원을 아낄 수 있어요."
                                },
                                {
                                  "title": "텀블러에 담아서 외출하기",
                                  "content": "집에서 내린 커피를 텀블러에 담고 나가면 자연스럽게 카페에 들를 필요가 없어져요. 준비된 커피가 있다는 것만으로도 충분히 만족스러워집니다."
                                }
                              ],
                              "word": {
                                "keyword": "기회비용",
                                "meaning": "어떤 선택을 할 때 포기해야 하는 다른 대안의 가치. 카페 커피를 포기하고 홈 드립을 선택함으로써 얻는 절약 효과"
                              }
                            },
                            {
                              "strategyId": 9,
                              "type": "CAF",
                              "title": "동료와 1잔 나눠 마시기(스플릿)",
                              "description": "큰 사이즈 하나를 동료와 나눠서 마시기",
                              "dailySaving": 2250,
                              "goalType": "DAILY",
                              "monthlySaving": 67500,
                              "dailyReducedDays": -2986.7,
                              "monthlyReducedDays": -2999.6,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "함께 커피 마실 동료 찾고 제안하기",
                                  "content": "직장 동료나 친구에게 '커피 한 잔 나눠 마실까?'라고 자연스럽게 제안해보세요. 의외로 좋아하는 사람들이 많을 거예요."
                                },
                                {
                                  "title": "큰 사이즈 주문하고 컵 2개 요청하기",
                                  "content": "벤티나 그란데 사이즈 하나를 주문하고 빈 컵을 하나 더 달라고 하면, 카페에서 대부분 무료로 줘요."
                                },
                                {
                                  "title": "번갈아가면서 비용 부담하기",
                                  "content": "이번엔 내가, 다음엔 상대방이 계산하는 식으로 하면 공평하게 절약할 수 있어요. 나눠 마시는 재미도 쏠쏠해요."
                                }
                              ],
                              "word": {
                                "keyword": "공동구매",
                                "meaning": "여러 명이 함께 구매해서 개별 비용을 줄이는 방법. 커피도 나눠 마시면 절약 가능"
                              }
                            },
                            {
                              "strategyId": 10,
                              "type": "CAF",
                              "title": "동네 2,500원 카페로 브랜드 다운시프트",
                              "description": "유명 브랜드 카페 대신 저렴한 동네 카페 이용",
                              "dailySaving": 2000,
                              "goalType": "DAILY",
                              "monthlySaving": 60000,
                              "dailyReducedDays": -2985.1,
                              "monthlyReducedDays": -2999.5,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "집/직장 근처 저렴한 카페 찾아보기",
                                  "content": "네이버 지도에서 '카페' 검색하고 가격대를 비교해보세요. 생각보다 괜찮은 동네 카페들이 많이 있을 거예요."
                                },
                                {
                                  "title": "맛과 분위기 미리 체험해보기",
                                  "content": "한두 번 가서 커피 맛과 분위기를 확인해보세요. 스타벅스만큼 좋다면 굳이 비싼 돈 낼 이유가 없죠."
                                },
                                {
                                  "title": "단골이 되어 추가 혜택 받기",
                                  "content": "자주 가면 사장님이 알아보시고 서비스로 사이즈업을 해주거나 쿠키를 하나 더 주시는 경우가 많아요."
                                }
                              ],
                              "word": {
                                "keyword": "브랜드 프리미엄",
                                "meaning": "브랜드 가치 때문에 지불하는 추가 비용. 브랜드보다 실용성을 택해 절약하는 전략"
                              }
                            },
                            {
                              "strategyId": 11,
                              "type": "CAF",
                              "title": "앱/영수증 쿠폰 사용",
                              "description": "카페 앱이나 영수증의 할인 쿠폰 적극 활용",
                              "dailySaving": 700,
                              "goalType": "DAILY",
                              "monthlySaving": 21000,
                              "dailyReducedDays": -2957.7,
                              "monthlyReducedDays": -2998.6,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "자주 가는 카페 앱 다운로드하고 쿠폰 확인하기",
                                  "content": "스타벅스, 이디야, 커피빈 등 자주 가는 카페 앱을 설치하고 쿠폰이나 이벤트 정보를 수시로 확인해보세요."
                                },
                                {
                                  "title": "주문 전 사용 가능한 쿠폰 체크하기",
                                  "content": "주문하기 전에 앱에서 사용할 수 있는 쿠폰이 있는지 꼭 확인하세요. 몇 초만 투자하면 500-1000원을 절약할 수 있어요."
                                },
                                {
                                  "title": "영수증 할인 쿠폰 보관하고 활용하기",
                                  "content": "영수증 뒷면에 할인 쿠폰이 있는 경우가 많아요. 지갑에 넣어두고 다음 방문 때 꼭 사용하세요."
                                }
                              ],
                              "word": {
                                "keyword": "할인쿠폰",
                                "meaning": "상품 구매 시 일정 금액을 할인받을 수 있는 증서. 작은 할인도 꾸준히 사용하면 큰 절약"
                              }
                            }
                          ]
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
                        "localDateTime": "2025-09-07T04:08:28.47472908",
                        "statusCode": 200,
                        "code": "SUCCESS",
                        "message": "전략 목록 조회에 성공하였습니다.",
                        "data": {
                          "totalCount": 10,
                          "list": [
                            {
                              "strategyId": 1,
                              "type": "CAF",
                              "title": "집/사무실 드립 1잔으로 대체",
                              "description": "카페 대신 집이나 사무실에서 직접 드립 커피를 내려 마시기",
                              "dailySaving": 3700,
                              "goalType": "DAILY",
                              "monthlySaving": 111000,
                              "dailyReducedDays": -2991.9,
                              "monthlyReducedDays": -2999.7,
                              "status": "DONE",
                              "howToStep": [
                                {
                                  "title": "원두와 드리퍼 구비하고 가격 차이 확인하기",
                                  "content": "카페 커피와 드립 커피의 가격 차이를 기록해 두면, 어느새 '오늘은 집에서 내려 마셔야겠다'라는 생각이 더 자주 들 거예요."
                                },
                                {
                                  "title": "매일 아침 홈 카페 루틴 만들기",
                                  "content": "아침 10분 일찍 일어나서 드립 커피를 내리는 루틴을 만들어보세요. 여유로운 시작과 함께 하루 3,700원을 아낄 수 있어요."
                                },
                                {
                                  "title": "텀블러에 담아서 외출하기",
                                  "content": "집에서 내린 커피를 텀블러에 담고 나가면 자연스럽게 카페에 들를 필요가 없어져요. 준비된 커피가 있다는 것만으로도 충분히 만족스러워집니다."
                                }
                              ],
                              "word": {
                                "keyword": "기회비용",
                                "meaning": "어떤 선택을 할 때 포기해야 하는 다른 대안의 가치. 카페 커피를 포기하고 홈 드립을 선택함으로써 얻는 절약 효과"
                              }
                            },
                            {
                              "strategyId": 9,
                              "type": "CAF",
                              "title": "동료와 1잔 나눠 마시기(스플릿)",
                              "description": "큰 사이즈 하나를 동료와 나눠서 마시기",
                              "dailySaving": 2250,
                              "goalType": "DAILY",
                              "monthlySaving": 67500,
                              "dailyReducedDays": -2986.7,
                              "monthlyReducedDays": -2999.6,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "함께 커피 마실 동료 찾고 제안하기",
                                  "content": "직장 동료나 친구에게 '커피 한 잔 나눠 마실까?'라고 자연스럽게 제안해보세요. 의외로 좋아하는 사람들이 많을 거예요."
                                },
                                {
                                  "title": "큰 사이즈 주문하고 컵 2개 요청하기",
                                  "content": "벤티나 그란데 사이즈 하나를 주문하고 빈 컵을 하나 더 달라고 하면, 카페에서 대부분 무료로 줘요."
                                },
                                {
                                  "title": "번갈아가면서 비용 부담하기",
                                  "content": "이번엔 내가, 다음엔 상대방이 계산하는 식으로 하면 공평하게 절약할 수 있어요. 나눠 마시는 재미도 쏠쏠해요."
                                }
                              ],
                              "word": {
                                "keyword": "공동구매",
                                "meaning": "여러 명이 함께 구매해서 개별 비용을 줄이는 방법. 커피도 나눠 마시면 절약 가능"
                              }
                            },
                            {
                              "strategyId": 10,
                              "type": "CAF",
                              "title": "동네 2,500원 카페로 브랜드 다운시프트",
                              "description": "유명 브랜드 카페 대신 저렴한 동네 카페 이용",
                              "dailySaving": 2000,
                              "goalType": "DAILY",
                              "monthlySaving": 60000,
                              "dailyReducedDays": -2985.1,
                              "monthlyReducedDays": -2999.5,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "집/직장 근처 저렴한 카페 찾아보기",
                                  "content": "네이버 지도에서 '카페' 검색하고 가격대를 비교해보세요. 생각보다 괜찮은 동네 카페들이 많이 있을 거예요."
                                },
                                {
                                  "title": "맛과 분위기 미리 체험해보기",
                                  "content": "한두 번 가서 커피 맛과 분위기를 확인해보세요. 스타벅스만큼 좋다면 굳이 비싼 돈 낼 이유가 없죠."
                                },
                                {
                                  "title": "단골이 되어 추가 혜택 받기",
                                  "content": "자주 가면 사장님이 알아보시고 서비스로 사이즈업을 해주거나 쿠키를 하나 더 주시는 경우가 많아요."
                                }
                              ],
                              "word": {
                                "keyword": "브랜드 프리미엄",
                                "meaning": "브랜드 가치 때문에 지불하는 추가 비용. 브랜드보다 실용성을 택해 절약하는 전략"
                              }
                            },
                            {
                              "strategyId": 11,
                              "type": "CAF",
                              "title": "앱/영수증 쿠폰 사용",
                              "description": "카페 앱이나 영수증의 할인 쿠폰 적극 활용",
                              "dailySaving": 700,
                              "goalType": "DAILY",
                              "monthlySaving": 21000,
                              "dailyReducedDays": -2957.7,
                              "monthlyReducedDays": -2998.6,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "자주 가는 카페 앱 다운로드하고 쿠폰 확인하기",
                                  "content": "스타벅스, 이디야, 커피빈 등 자주 가는 카페 앱을 설치하고 쿠폰이나 이벤트 정보를 수시로 확인해보세요."
                                },
                                {
                                  "title": "주문 전 사용 가능한 쿠폰 체크하기",
                                  "content": "주문하기 전에 앱에서 사용할 수 있는 쿠폰이 있는지 꼭 확인하세요. 몇 초만 투자하면 500-1000원을 절약할 수 있어요."
                                },
                                {
                                  "title": "영수증 할인 쿠폰 보관하고 활용하기",
                                  "content": "영수증 뒷면에 할인 쿠폰이 있는 경우가 많아요. 지갑에 넣어두고 다음 방문 때 꼭 사용하세요."
                                }
                              ],
                              "word": {
                                "keyword": "할인쿠폰",
                                "meaning": "상품 구매 시 일정 금액을 할인받을 수 있는 증서. 작은 할인도 꾸준히 사용하면 큰 절약"
                              }
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
      description = "도전 시작하기 성공 시",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value =
                  """
                      {
                        "localDateTime": "2025-09-06T17:35:58.96475",
                        "statusCode": 200,
                        "code": "SUCCESS",
                        "message": null,
                        "data": "도전 시작에 성공하였습니다."
                      }
                  """
          )
      )
  )
  @Operation(summary = "도전 시작하기 api 입니다.", description = "도전 시작하기 버튼을 누르면 호출되는 api 입니다.")
  public ResponseDTO<String> startStrategy(Long strategyId, HttpServletRequest httpServletRequest);


  @ApiResponse(
      responseCode = "200",
      description = "조회 성공 시",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value =
                  """
                  {
                        "localDateTime": "2025-09-07T04:08:28.47472908",
                        "statusCode": 200,
                        "code": "SUCCESS",
                        "message": "전략 목록 조회에 성공하였습니다.",
                        "data": {
                          "totalCount": 10,
                          "list": [
                            {
                              "strategyId": 1,
                              "type": "CAF",
                              "title": "집/사무실 드립 1잔으로 대체",
                              "description": "카페 대신 집이나 사무실에서 직접 드립 커피를 내려 마시기",
                              "dailySaving": 3700,
                              "goalType": "DAILY",
                              "monthlySaving": 111000,
                              "dailyReducedDays": -2991.9,
                              "monthlyReducedDays": -2999.7,
                              "status": "RUNNING",
                              "howToStep": [
                                {
                                  "title": "원두와 드리퍼 구비하고 가격 차이 확인하기",
                                  "content": "카페 커피와 드립 커피의 가격 차이를 기록해 두면, 어느새 '오늘은 집에서 내려 마셔야겠다'라는 생각이 더 자주 들 거예요."
                                },
                                {
                                  "title": "매일 아침 홈 카페 루틴 만들기",
                                  "content": "아침 10분 일찍 일어나서 드립 커피를 내리는 루틴을 만들어보세요. 여유로운 시작과 함께 하루 3,700원을 아낄 수 있어요."
                                },
                                {
                                  "title": "텀블러에 담아서 외출하기",
                                  "content": "집에서 내린 커피를 텀블러에 담고 나가면 자연스럽게 카페에 들를 필요가 없어져요. 준비된 커피가 있다는 것만으로도 충분히 만족스러워집니다."
                                }
                              ],
                              "word": {
                                "keyword": "기회비용",
                                "meaning": "어떤 선택을 할 때 포기해야 하는 다른 대안의 가치. 카페 커피를 포기하고 홈 드립을 선택함으로써 얻는 절약 효과"
                              }
                            },
                            {
                              "strategyId": 9,
                              "type": "CAF",
                              "title": "동료와 1잔 나눠 마시기(스플릿)",
                              "description": "큰 사이즈 하나를 동료와 나눠서 마시기",
                              "dailySaving": 2250,
                              "goalType": "DAILY",
                              "monthlySaving": 67500,
                              "dailyReducedDays": -2986.7,
                              "monthlyReducedDays": -2999.6,
                              "status": "RUNNING",
                              "howToStep": [
                                {
                                  "title": "함께 커피 마실 동료 찾고 제안하기",
                                  "content": "직장 동료나 친구에게 '커피 한 잔 나눠 마실까?'라고 자연스럽게 제안해보세요. 의외로 좋아하는 사람들이 많을 거예요."
                                },
                                {
                                  "title": "큰 사이즈 주문하고 컵 2개 요청하기",
                                  "content": "벤티나 그란데 사이즈 하나를 주문하고 빈 컵을 하나 더 달라고 하면, 카페에서 대부분 무료로 줘요."
                                },
                                {
                                  "title": "번갈아가면서 비용 부담하기",
                                  "content": "이번엔 내가, 다음엔 상대방이 계산하는 식으로 하면 공평하게 절약할 수 있어요. 나눠 마시는 재미도 쏠쏠해요."
                                }
                              ],
                              "word": {
                                "keyword": "공동구매",
                                "meaning": "여러 명이 함께 구매해서 개별 비용을 줄이는 방법. 커피도 나눠 마시면 절약 가능"
                              }
                            },
                            {
                              "strategyId": 10,
                              "type": "CAF",
                              "title": "동네 2,500원 카페로 브랜드 다운시프트",
                              "description": "유명 브랜드 카페 대신 저렴한 동네 카페 이용",
                              "dailySaving": 2000,
                              "goalType": "DAILY",
                              "monthlySaving": 60000,
                              "dailyReducedDays": -2985.1,
                              "monthlyReducedDays": -2999.5,
                              "status": "RUNNING",
                              "howToStep": [
                                {
                                  "title": "집/직장 근처 저렴한 카페 찾아보기",
                                  "content": "네이버 지도에서 '카페' 검색하고 가격대를 비교해보세요. 생각보다 괜찮은 동네 카페들이 많이 있을 거예요."
                                },
                                {
                                  "title": "맛과 분위기 미리 체험해보기",
                                  "content": "한두 번 가서 커피 맛과 분위기를 확인해보세요. 스타벅스만큼 좋다면 굳이 비싼 돈 낼 이유가 없죠."
                                },
                                {
                                  "title": "단골이 되어 추가 혜택 받기",
                                  "content": "자주 가면 사장님이 알아보시고 서비스로 사이즈업을 해주거나 쿠키를 하나 더 주시는 경우가 많아요."
                                }
                              ],
                              "word": {
                                "keyword": "브랜드 프리미엄",
                                "meaning": "브랜드 가치 때문에 지불하는 추가 비용. 브랜드보다 실용성을 택해 절약하는 전략"
                              }
                            },
                            {
                              "strategyId": 11,
                              "type": "CAF",
                              "title": "앱/영수증 쿠폰 사용",
                              "description": "카페 앱이나 영수증의 할인 쿠폰 적극 활용",
                              "dailySaving": 700,
                              "goalType": "DAILY",
                              "monthlySaving": 21000,
                              "dailyReducedDays": -2957.7,
                              "monthlyReducedDays": -2998.6,
                              "status": "RUNNING",
                              "howToStep": [
                                {
                                  "title": "자주 가는 카페 앱 다운로드하고 쿠폰 확인하기",
                                  "content": "스타벅스, 이디야, 커피빈 등 자주 가는 카페 앱을 설치하고 쿠폰이나 이벤트 정보를 수시로 확인해보세요."
                                },
                                {
                                  "title": "주문 전 사용 가능한 쿠폰 체크하기",
                                  "content": "주문하기 전에 앱에서 사용할 수 있는 쿠폰이 있는지 꼭 확인하세요. 몇 초만 투자하면 500-1000원을 절약할 수 있어요."
                                },
                                {
                                  "title": "영수증 할인 쿠폰 보관하고 활용하기",
                                  "content": "영수증 뒷면에 할인 쿠폰이 있는 경우가 많아요. 지갑에 넣어두고 다음 방문 때 꼭 사용하세요."
                                }
                              ],
                              "word": {
                                "keyword": "할인쿠폰",
                                "meaning": "상품 구매 시 일정 금액을 할인받을 수 있는 증서. 작은 할인도 꾸준히 사용하면 큰 절약"
                              }
                            }
                          ]
                        }
                      }
                  """
          )
      )
  )
  @Operation(summary = "유저가 진행중인 도전을 전부 조회하는 api 입니다.", description = "요청에 토큰이 필요합니다.")
  public ResponseDTO<ListResponseDTO<List<StrategyDataDTO>>> getStrategiesByrunning(HttpServletRequest httpServletRequest);


  @ApiResponse(
      responseCode = "200",
      description = "도전 종료하기 성공 시",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value =
                  """
                      {
                        "localDateTime": "2025-09-07T03:41:31.011853",
                        "statusCode": 200,
                        "code": "SUCCESS",
                        "message": "진행중인 전략이 종료되었습니다.",
                        "data": null
                      }
                  """
          )
      )
  )
  @Operation(summary = "도전 끝내기 api 입니다.", description = "진행중인 도전을 완료시키는 api 입니다.")
  public ResponseDTO<String> endStrategy(Long strategyId, HttpServletRequest httpServletRequest);

}
