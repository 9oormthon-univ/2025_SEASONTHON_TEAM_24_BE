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
                        "localDateTime": "2025-09-06T17:36:03.733653",
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
                              "dailyReducedDays": -2361.7,
                              "monthlyReducedDays": -2973.2,
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
                              "word": "기회비용: 어떤 선택을 할 때 포기해야 하는 다른 대안의 가치. 카페 커피를 포기하고 홈 드립을 선택함으로써 얻는 절약 효과"
                            },
                            {
                              "strategyId": 3,
                              "type": "CAF",
                              "title": "편의점 원두커피로 대체",
                              "description": "브랜드 카페 대신 편의점의 저렴한 원두커피로 변경",
                              "dailySaving": 3000,
                              "goalType": "DAILY",
                              "monthlySaving": 90000,
                              "dailyReducedDays": -2250,
                              "monthlyReducedDays": -2967,
                              "status": "RUNNING",
                              "howToStep": [
                                {
                                  "title": "근처 편의점 원두커피 맛 테스트하기",
                                  "content": "평소 가던 카페 대신 편의점 몇 곳의 원두커피를 마셔보세요. 생각보다 맛이 괜찮다는 걸 확인하면 바꾸기가 훨씬 쉬워져요."
                                },
                                {
                                  "title": "편의점 멤버십으로 추가 할인받기",
                                  "content": "세븐일레븐, CU 등의 멤버십에 가입하면 원두커피를 더 저렴하게 마실 수 있어요. 적립 포인트로 나중에 공짜 커피도 가능해집니다."
                                },
                                {
                                  "title": "카페 방문 욕구 생길 때 편의점으로 방향 바꾸기",
                                  "content": "스타벅스 가고 싶을 때 '잠깐, 편의점 커피도 나쁘지 않았는데'라고 생각해보세요. 3,000원 절약과 함께 더 빠르게 커피를 마실 수 있어요."
                                }
                              ],
                              "word": "대체재: 비슷한 기능을 하는 다른 상품. 브랜드 카페와 편의점 커피는 서로 대체재 관계"
                            },
                            {
                              "strategyId": 4,
                              "type": "CAF",
                              "title": "라떼→아메리카노로 변경",
                              "description": "우유가 들어간 라떼류 대신 아메리카노로 메뉴 변경",
                              "dailySaving": 1500,
                              "goalType": "DAILY",
                              "monthlySaving": 45000,
                              "dailyReducedDays": -1800,
                              "monthlyReducedDays": -2934.8,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "평소 주문 메뉴 점검하고 가격 비교하기",
                                  "content": "라떼와 아메리카노의 가격 차이를 계산해보면 한 달에 얼마나 절약되는지 명확해져요. 작은 차이도 누적되면 큰 금액이 됩니다."
                                },
                                {
                                  "title": "아메리카노 맛에 적응하는 기간 갖기",
                                  "content": "처음에는 밍밍하게 느껴질 수 있지만, 일주일 정도 꾸준히 마시면 원두 본연의 맛을 즐길 수 있게 되어요."
                                },
                                {
                                  "title": "단맛이 그리울 때만 가끔 라떼 주문하기",
                                  "content": "매일 라떼 대신 주 1-2회만 라떼를 마시는 것으로 조절하면, 특별함도 느끼면서 절약 효과도 유지할 수 있어요."
                                }
                              ],
                              "word": "소비습관: 반복적으로 나타나는 소비 패턴. 메뉴 선택 습관을 바꿔 지출을 줄이는 방법"
                            },
                            {
                              "strategyId": 5,
                              "type": "CAF",
                              "title": "사이즈 다운(Tall 선택)",
                              "description": "큰 사이즈 대신 가장 작은 사이즈로 주문하기",
                              "dailySaving": 1000,
                              "goalType": "DAILY",
                              "monthlySaving": 30000,
                              "dailyReducedDays": -1500,
                              "monthlyReducedDays": -2903.2,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "한 단계씩 사이즈 줄여보기",
                                  "content": "갑자기 가장 작은 사이즈로 바꾸면 아쉬울 수 있으니, 그란데를 마시던 분은 먼저 벤티로, 그 다음 톨로 단계적으로 줄여보세요."
                                },
                                {
                                  "title": "Tall 사이즈로 주문 고정하기",
                                  "content": "Tall 사이즈가 생각보다 충분하다는 걸 경험하면, 더 큰 사이즈가 불필요했다는 걸 깨닫게 되어요."
                                },
                                {
                                  "title": "부족하면 물이나 차로 보충하기",
                                  "content": "커피가 부족하다고 느끼면 물을 마시거나 무료 차를 추가로 마셔보세요. 카페인 섭취량도 조절되고 건강에도 좋아요."
                                }
                              ],
                              "word": "다운사이징: 제품이나 서비스의 크기를 줄여서 비용을 절약하는 소비 전략"
                            },
                            {
                              "strategyId": 6,
                              "type": "CAF",
                              "title": "시럽/샷 추가 안함",
                              "description": "바닐라 시럽이나 샷 추가 등의 유료 옵션 제거",
                              "dailySaving": 600,
                              "goalType": "DAILY",
                              "monthlySaving": 18000,
                              "dailyReducedDays": -1125,
                              "monthlyReducedDays": -2842.1,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "평소 추가하던 옵션의 누적 비용 계산하기",
                                  "content": "시럽 추가 500원이 한 달이면 15,000원, 1년이면 18만원이라는 걸 계산해보면 옵션 없이도 충분하다는 생각이 들어요."
                                },
                                {
                                  "title": "기본 메뉴로만 주문해보기",
                                  "content": "일주일 동안 기본 메뉴만 주문해보세요. 의외로 원래 맛이 더 깔끔하고 좋다는 걸 발견할 수 있어요."
                                },
                                {
                                  "title": "단맛 필요하면 무료 설탕 활용하기",
                                  "content": "카페에 비치된 무료 설탕이나 시럽을 이용하면 추가 비용 없이 단맛을 더할 수 있어요."
                                }
                              ],
                              "word": "추가비용: 기본 가격에 더해지는 부가 요금. 작은 추가비용들이 누적되면 큰 지출이 됨"
                            },
                            {
                              "strategyId": 7,
                              "type": "CAF",
                              "title": "텀블러 할인 받기",
                              "description": "개인 텀블러 지참 시 받을 수 있는 할인 혜택 활용",
                              "dailySaving": 300,
                              "goalType": "DAILY",
                              "monthlySaving": 9000,
                              "dailyReducedDays": -692.3,
                              "monthlyReducedDays": -2700,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "텀블러를 가방에 항상 넣고 다니기",
                                  "content": "텀블러를 잊고 나오는 일이 없도록 가방에 미리 넣어두세요. 습관이 되면 자연스럽게 할인을 받을 수 있어요."
                                },
                                {
                                  "title": "할인 혜택이 있는 카페 우선 선택하기",
                                  "content": "스타벅스는 300원, 이디야는 200원 할인해줘요. 텀블러 할인이 더 큰 카페를 우선적으로 이용해보세요."
                                },
                                {
                                  "title": "환경보호 실천으로 뿌듯함까지 얻기",
                                  "content": "할인받는 것도 좋지만, 일회용 컵을 줄여서 환경을 보호한다는 뿌듯함도 함께 얻을 수 있어요."
                                }
                              ],
                              "word": "친환경 소비: 환경보호를 고려한 소비 행태. 일회용품 사용을 줄이면서 할인 혜택도 받는 방법"
                            },
                            {
                              "strategyId": 8,
                              "type": "CAF",
                              "title": "디저트(케이크/쿠키) 빼기",
                              "description": "커피와 함께 주문하던 디저트류 구매 중단",
                              "dailySaving": 500,
                              "goalType": "DAILY",
                              "monthlySaving": 15000,
                              "dailyReducedDays": -1000,
                              "monthlyReducedDays": -2812.5,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "디저트 구매 패턴과 빈도 파악하기",
                                  "content": "일주일에 몇 번, 어떤 상황에서 디저트를 사는지 기록해보세요. 패턴을 알면 미리 대비할 수 있어요."
                                },
                                {
                                  "title": "커피만 주문하기로 미리 결정하기",
                                  "content": "카페에 들어가기 전에 '오늘은 커피만'이라고 미리 정해두면 충동적으로 디저트를 주문할 확률이 줄어들어요."
                                },
                                {
                                  "title": "집에서 준비한 간식으로 대체하기",
                                  "content": "단것이 당기면 집에서 미리 준비한 과자나 과일을 먹어보세요. 카페 디저트보다 건강하고 경제적이에요."
                                }
                              ],
                              "word": "충동구매: 미리 계획하지 않고 순간적인 욕구로 하는 구매. 커피와 함께 보이는 디저트에 대한 충동구매 방지"
                            },
                            {
                              "strategyId": 11,
                              "type": "CAF",
                              "title": "앱/영수증 쿠폰 사용",
                              "description": "카페 앱이나 영수증의 할인 쿠폰 적극 활용",
                              "dailySaving": 700,
                              "goalType": "DAILY",
                              "monthlySaving": 21000,
                              "dailyReducedDays": -1235.3,
                              "monthlyReducedDays": -2863.6,
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
                              "word": "할인쿠폰: 상품 구매 시 일정 금액을 할인받을 수 있는 증서. 작은 할인도 꾸준히 사용하면 큰 절약"
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
                        "localDateTime": "2025-09-06T17:36:03.733653",
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
                              "dailyReducedDays": -2361.7,
                              "monthlyReducedDays": -2973.2,
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
                              "word": "기회비용: 어떤 선택을 할 때 포기해야 하는 다른 대안의 가치. 카페 커피를 포기하고 홈 드립을 선택함으로써 얻는 절약 효과"
                            },
                            {
                              "strategyId": 3,
                              "type": "CAF",
                              "title": "편의점 원두커피로 대체",
                              "description": "브랜드 카페 대신 편의점의 저렴한 원두커피로 변경",
                              "dailySaving": 3000,
                              "goalType": "DAILY",
                              "monthlySaving": 90000,
                              "dailyReducedDays": -2250,
                              "monthlyReducedDays": -2967,
                              "status": "RUNNING",
                              "howToStep": [
                                {
                                  "title": "근처 편의점 원두커피 맛 테스트하기",
                                  "content": "평소 가던 카페 대신 편의점 몇 곳의 원두커피를 마셔보세요. 생각보다 맛이 괜찮다는 걸 확인하면 바꾸기가 훨씬 쉬워져요."
                                },
                                {
                                  "title": "편의점 멤버십으로 추가 할인받기",
                                  "content": "세븐일레븐, CU 등의 멤버십에 가입하면 원두커피를 더 저렴하게 마실 수 있어요. 적립 포인트로 나중에 공짜 커피도 가능해집니다."
                                },
                                {
                                  "title": "카페 방문 욕구 생길 때 편의점으로 방향 바꾸기",
                                  "content": "스타벅스 가고 싶을 때 '잠깐, 편의점 커피도 나쁘지 않았는데'라고 생각해보세요. 3,000원 절약과 함께 더 빠르게 커피를 마실 수 있어요."
                                }
                              ],
                              "word": "대체재: 비슷한 기능을 하는 다른 상품. 브랜드 카페와 편의점 커피는 서로 대체재 관계"
                            },
                            {
                              "strategyId": 4,
                              "type": "CAF",
                              "title": "라떼→아메리카노로 변경",
                              "description": "우유가 들어간 라떼류 대신 아메리카노로 메뉴 변경",
                              "dailySaving": 1500,
                              "goalType": "DAILY",
                              "monthlySaving": 45000,
                              "dailyReducedDays": -1800,
                              "monthlyReducedDays": -2934.8,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "평소 주문 메뉴 점검하고 가격 비교하기",
                                  "content": "라떼와 아메리카노의 가격 차이를 계산해보면 한 달에 얼마나 절약되는지 명확해져요. 작은 차이도 누적되면 큰 금액이 됩니다."
                                },
                                {
                                  "title": "아메리카노 맛에 적응하는 기간 갖기",
                                  "content": "처음에는 밍밍하게 느껴질 수 있지만, 일주일 정도 꾸준히 마시면 원두 본연의 맛을 즐길 수 있게 되어요."
                                },
                                {
                                  "title": "단맛이 그리울 때만 가끔 라떼 주문하기",
                                  "content": "매일 라떼 대신 주 1-2회만 라떼를 마시는 것으로 조절하면, 특별함도 느끼면서 절약 효과도 유지할 수 있어요."
                                }
                              ],
                              "word": "소비습관: 반복적으로 나타나는 소비 패턴. 메뉴 선택 습관을 바꿔 지출을 줄이는 방법"
                            },
                            {
                              "strategyId": 5,
                              "type": "CAF",
                              "title": "사이즈 다운(Tall 선택)",
                              "description": "큰 사이즈 대신 가장 작은 사이즈로 주문하기",
                              "dailySaving": 1000,
                              "goalType": "DAILY",
                              "monthlySaving": 30000,
                              "dailyReducedDays": -1500,
                              "monthlyReducedDays": -2903.2,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "한 단계씩 사이즈 줄여보기",
                                  "content": "갑자기 가장 작은 사이즈로 바꾸면 아쉬울 수 있으니, 그란데를 마시던 분은 먼저 벤티로, 그 다음 톨로 단계적으로 줄여보세요."
                                },
                                {
                                  "title": "Tall 사이즈로 주문 고정하기",
                                  "content": "Tall 사이즈가 생각보다 충분하다는 걸 경험하면, 더 큰 사이즈가 불필요했다는 걸 깨닫게 되어요."
                                },
                                {
                                  "title": "부족하면 물이나 차로 보충하기",
                                  "content": "커피가 부족하다고 느끼면 물을 마시거나 무료 차를 추가로 마셔보세요. 카페인 섭취량도 조절되고 건강에도 좋아요."
                                }
                              ],
                              "word": "다운사이징: 제품이나 서비스의 크기를 줄여서 비용을 절약하는 소비 전략"
                            },
                            {
                              "strategyId": 6,
                              "type": "CAF",
                              "title": "시럽/샷 추가 안함",
                              "description": "바닐라 시럽이나 샷 추가 등의 유료 옵션 제거",
                              "dailySaving": 600,
                              "goalType": "DAILY",
                              "monthlySaving": 18000,
                              "dailyReducedDays": -1125,
                              "monthlyReducedDays": -2842.1,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "평소 추가하던 옵션의 누적 비용 계산하기",
                                  "content": "시럽 추가 500원이 한 달이면 15,000원, 1년이면 18만원이라는 걸 계산해보면 옵션 없이도 충분하다는 생각이 들어요."
                                },
                                {
                                  "title": "기본 메뉴로만 주문해보기",
                                  "content": "일주일 동안 기본 메뉴만 주문해보세요. 의외로 원래 맛이 더 깔끔하고 좋다는 걸 발견할 수 있어요."
                                },
                                {
                                  "title": "단맛 필요하면 무료 설탕 활용하기",
                                  "content": "카페에 비치된 무료 설탕이나 시럽을 이용하면 추가 비용 없이 단맛을 더할 수 있어요."
                                }
                              ],
                              "word": "추가비용: 기본 가격에 더해지는 부가 요금. 작은 추가비용들이 누적되면 큰 지출이 됨"
                            },
                            {
                              "strategyId": 7,
                              "type": "CAF",
                              "title": "텀블러 할인 받기",
                              "description": "개인 텀블러 지참 시 받을 수 있는 할인 혜택 활용",
                              "dailySaving": 300,
                              "goalType": "DAILY",
                              "monthlySaving": 9000,
                              "dailyReducedDays": -692.3,
                              "monthlyReducedDays": -2700,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "텀블러를 가방에 항상 넣고 다니기",
                                  "content": "텀블러를 잊고 나오는 일이 없도록 가방에 미리 넣어두세요. 습관이 되면 자연스럽게 할인을 받을 수 있어요."
                                },
                                {
                                  "title": "할인 혜택이 있는 카페 우선 선택하기",
                                  "content": "스타벅스는 300원, 이디야는 200원 할인해줘요. 텀블러 할인이 더 큰 카페를 우선적으로 이용해보세요."
                                },
                                {
                                  "title": "환경보호 실천으로 뿌듯함까지 얻기",
                                  "content": "할인받는 것도 좋지만, 일회용 컵을 줄여서 환경을 보호한다는 뿌듯함도 함께 얻을 수 있어요."
                                }
                              ],
                              "word": "친환경 소비: 환경보호를 고려한 소비 행태. 일회용품 사용을 줄이면서 할인 혜택도 받는 방법"
                            },
                            {
                              "strategyId": 8,
                              "type": "CAF",
                              "title": "디저트(케이크/쿠키) 빼기",
                              "description": "커피와 함께 주문하던 디저트류 구매 중단",
                              "dailySaving": 500,
                              "goalType": "DAILY",
                              "monthlySaving": 15000,
                              "dailyReducedDays": -1000,
                              "monthlyReducedDays": -2812.5,
                              "status": "WAITING",
                              "howToStep": [
                                {
                                  "title": "디저트 구매 패턴과 빈도 파악하기",
                                  "content": "일주일에 몇 번, 어떤 상황에서 디저트를 사는지 기록해보세요. 패턴을 알면 미리 대비할 수 있어요."
                                },
                                {
                                  "title": "커피만 주문하기로 미리 결정하기",
                                  "content": "카페에 들어가기 전에 '오늘은 커피만'이라고 미리 정해두면 충동적으로 디저트를 주문할 확률이 줄어들어요."
                                },
                                {
                                  "title": "집에서 준비한 간식으로 대체하기",
                                  "content": "단것이 당기면 집에서 미리 준비한 과자나 과일을 먹어보세요. 카페 디저트보다 건강하고 경제적이에요."
                                }
                              ],
                              "word": "충동구매: 미리 계획하지 않고 순간적인 욕구로 하는 구매. 커피와 함께 보이는 디저트에 대한 충동구매 방지"
                            },
                            {
                              "strategyId": 11,
                              "type": "CAF",
                              "title": "앱/영수증 쿠폰 사용",
                              "description": "카페 앱이나 영수증의 할인 쿠폰 적극 활용",
                              "dailySaving": 700,
                              "goalType": "DAILY",
                              "monthlySaving": 21000,
                              "dailyReducedDays": -1235.3,
                              "monthlyReducedDays": -2863.6,
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
                              "word": "할인쿠폰: 상품 구매 시 일정 금액을 할인받을 수 있는 증서. 작은 할인도 꾸준히 사용하면 큰 절약"
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
                        "localDateTime": "2025-09-06T23:11:36.978056",
                        "statusCode": 200,
                        "code": "SUCCESS",
                        "message": "전략 목록 조회에 성공하였습니다.",
                        "data": {
                          "totalCount": 2,
                          "list": [
                            {
                              "strategyId": 1,
                              "type": "CAF",
                              "title": "집/사무실 드립 1잔으로 대체",
                              "description": "카페 대신 집이나 사무실에서 직접 드립 커피를 내려 마시기",
                              "dailySaving": 3700,
                              "goalType": "DAILY",
                              "monthlySaving": 111000,
                              "dailyReducedDays": -299.2,
                              "monthlyReducedDays": -300,
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
                              "word": "기회비용: 어떤 선택을 할 때 포기해야 하는 다른 대안의 가치. 카페 커피를 포기하고 홈 드립을 선택함으로써 얻는 절약 효과"
                            },
                            {
                              "strategyId": 3,
                              "type": "CAF",
                              "title": "편의점 원두커피로 대체",
                              "description": "브랜드 카페 대신 편의점의 저렴한 원두커피로 변경",
                              "dailySaving": 3000,
                              "goalType": "DAILY",
                              "monthlySaving": 90000,
                              "dailyReducedDays": -299,
                              "monthlyReducedDays": -300,
                              "status": "RUNNING",
                              "howToStep": [
                                {
                                  "title": "근처 편의점 원두커피 맛 테스트하기",
                                  "content": "평소 가던 카페 대신 편의점 몇 곳의 원두커피를 마셔보세요. 생각보다 맛이 괜찮다는 걸 확인하면 바꾸기가 훨씬 쉬워져요."
                                },
                                {
                                  "title": "편의점 멤버십으로 추가 할인받기",
                                  "content": "세븐일레븐, CU 등의 멤버십에 가입하면 원두커피를 더 저렴하게 마실 수 있어요. 적립 포인트로 나중에 공짜 커피도 가능해집니다."
                                },
                                {
                                  "title": "카페 방문 욕구 생길 때 편의점으로 방향 바꾸기",
                                  "content": "스타벅스 가고 싶을 때 '잠깐, 편의점 커피도 나쁘지 않았는데'라고 생각해보세요. 3,000원 절약과 함께 더 빠르게 커피를 마실 수 있어요."
                                }
                              ],
                              "word": "대체재: 비슷한 기능을 하는 다른 상품. 브랜드 카페와 편의점 커피는 서로 대체재 관계"
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
  @Operation(summary = "도전 끝내기 api 입니다.", description = "진행중인 도전을 완료시키는 api 입니다.")
  public ResponseDTO<String> endStrategy(Long strategyId, HttpServletRequest httpServletRequest);

}
