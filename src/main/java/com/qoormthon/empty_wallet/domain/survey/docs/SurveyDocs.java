package com.qoormthon.empty_wallet.domain.survey.docs;

import com.qoormthon.empty_wallet.domain.survey.dto.request.SubmitSurveyRequest;
import com.qoormthon.empty_wallet.domain.survey.dto.response.SubmitSurveyResponse;
import com.qoormthon.empty_wallet.domain.survey.dto.response.SurveyBundleResponse;
import com.qoormthon.empty_wallet.global.common.dto.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "설문 API", description = "풀/퀵 설문 조회 및 제출")
public interface SurveyDocs {

    // FULL 설문 조회
    @Operation(summary = "풀 서베이 번들 조회",
            description = "질문(title)과 보기(type, title) 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class),
                    examples = @ExampleObject(
                            name = "FullSurveySuccess",
                            value = """
                {
                  "localDateTime":"2025-09-02T10:00:00",
                  "statusCode":200,"code":"SUCCESS",
                  "message":"FULL 설문 조회 성공",
                  "data":{"type":"FULL","questionCount":10,"questions":[
                    {"surveyId":1,"title":"...","options":[{"type":"A","title":"..."},{"type":"B","title":"..."}]}
                  ]}
                }
              """
                    )))
    ResponseDTO<SurveyBundleResponse> getFull();

    // QUICK 설문 조회 (시그니처를 컨트롤러와 동일하게 맞추기!)
    @Operation(summary = "퀵 서베이 번들 조회",
            description = "질문(title)과 보기(type, title) 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "QuickSurveySuccess",
                                    value = """
                                {
                                  "localDateTime": "2025-09-02T10:00:00",
                                  "statusCode": 200,
                                  "code": "SUCCESS",
                                  "message": "QUICK 설문 조회 성공",
                                  "data": {
                                    "type": "QUICK",
                                    "questionCount": 1,
                                    "questions": [
                                      {
                                        "surveyId": 11,
                                        "title": "지난주랑 비교하면, 이번 주 소비는 어땠나요?",
                                        "options": [
                                          { "type": "A", "title": "많이 줄었어요" },
                                          { "type": "B", "title": "조금 줄었어요" }
                                        ]
                                      }
                                    ]
                                  }
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "토큰 자체는 수신되었으나 해당 사용자 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "UserNotFound",
                                    value = """
                                {
                                  "localDateTime": "2025-09-04T00:17:22.7799144",
                                  "statusCode": 404,
                                  "code": "USER_NOT_FOUND",
                                  "message": "해당 사용자를 찾을 수 없습니다.",
                                  "data": null
                                }
                                """
                            )
                    )
            )
            // 필요하면 401도 같이 문서화 가능
            ,@ApiResponse(
            responseCode = "401",
            description = "인증 실패(토큰 없음/만료/서명 오류 등)",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class),
                    examples = @ExampleObject(
                            name = "TokenInvalid",
                            value = """
                         {
                           "localDateTime": "2025-09-04T00:17:22.7799144",
                           "statusCode": 401,
                           "code": "TOKEN_INVALID",
                           "message": "유효하지 않은 토큰입니다.",
                           "data": null
                         }
                         """
                    )
            )
    )
    })
    ResponseDTO<SurveyBundleResponse> getQuick(
            @Parameter(hidden = true) Long userId   // AuthenticationPrincipal로 주입됨(문서 비노출)
    );

    // 설문 제출(검증)
    @Operation(summary = "설문 제출(검증)",
            description = """
        요청으로 넘어온 답들을 검증해 설문 종류(type), 완료(completed)여부와 저장된 개수(savedCount)를 반환합니다.
      """)
    @RequestBody(required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SubmitSurveyRequest.class),
                    examples = {
                            @ExampleObject(name = "SubmitFull", value = """
              {"type":"FULL","answers":[
                {"surveyId":1,"optionType":"A"},
                {"surveyId":2,"optionType":"B"}
              ]}
            """)
                    }))
    @ApiResponse(responseCode = "200", description = "검증 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class),
                    examples = @ExampleObject(name = "SubmitSuccess",
                            value = """
                {"localDateTime":"2025-09-02T10:00:00","statusCode":200,"code":"SUCCESS",
                 "message":"설문 검증 성공","data":{"completed":false,"savedCount":7}}
              """)))
    ResponseDTO<SubmitSurveyResponse> submit(
            @Parameter(hidden = true) Long userId, SubmitSurveyRequest request);
}