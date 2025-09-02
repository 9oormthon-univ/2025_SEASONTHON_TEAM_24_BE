package com.qoormthon.empty_wallet.domain.survey.docs;

import com.qoormthon.empty_wallet.domain.survey.dto.request.SubmitSurveyRequest;
import com.qoormthon.empty_wallet.domain.survey.dto.response.SubmitSurveyResponse;
import com.qoormthon.empty_wallet.domain.survey.dto.response.SurveyBundleResponse;
import com.qoormthon.empty_wallet.global.common.dto.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "설문 API", description = "풀/퀵 설문 조회 및 제출")
public interface SurveyDocs {

    /* =========================
     *  FULL 설문 조회
     * ========================= */
    @Operation(
            summary = "풀 서베이 번들 조회",
            description = "질문(title)과 보기(type, title) 목록을 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class),
                    examples = @ExampleObject(
                            name = "FullSurveySuccess",
                            value = """
                  {
                    "localDateTime": "2025-09-02T10:00:00",
                    "statusCode": 200,
                    "code": "SUCCESS",
                    "message": "FULL 설문 조회 성공",
                    "data": {
                      "type": "FULL",
                      "questionCount": 10,
                      "questions": [
                        {
                          "surveyId": 1,
                          "title": "주말 아침에 눈 떴을 때, 제일 먼저 하는 생각은?",
                          "options": [
                            {"type": "A", "title": "오늘은 커피 내리고 집에서 느긋하게 보내야지"},
                            {"type": "B", "title": "오늘 뭐하지? 나가서 재밌는 거 해야겠다"}
                          ]
                        }
                      ]
                    }
                  }
                  """
                    )
            )
    )
    ResponseDTO<SurveyBundleResponse> getFull();

    /* =========================
     *  QUICK 설문 조회
     * ========================= */
    @Operation(
            summary = "퀵 서베이 번들 조회",
            description = "질문(title)과 보기(type, title) 목록을 반환합니다".
    )
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
                      "questionCount": 3,
                      "questions": [
                        {
                          "surveyId": 11,
                          "title": "지난주랑 비교하면, 이번 주 소비는 어땠나요?",
                          "options": [
                            {"type": "A", "title": "많이 줄었어요"},
                            {"type": "B", "title": "조금 줄었어요"},
                            {"type": "C", "title": "비슷했어요"},
                            {"type": "D", "title": "조금 늘었어요"},
                            {"type": "E", "title": "많이 늘었어요"}
                          ]
                        }
                      ]
                    }
                  }
                  """
                    )
            )
    )
    ResponseDTO<SurveyBundleResponse> getQuick();

    /* =========================
     *  설문 제출 (검증 전용)
     * ========================= */
    @Operation(
            summary = "설문 제출(검증)",
            description = """
          요청으로 넘어온 답들을 검증해 완료(completed) 여부와 저장된 개수(savedCount)를 반환합니다.
          QUICK Q3처럼 동일 (surveyId, type)이 여러 옵션으로 존재할 수 있으므로, 필요한 경우 code를 함께 보내 주세요.
          """
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SubmitSurveyRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "SubmitFull",
                                    summary = "FULL 제출 예시",
                                    value = """
                      {
                        "type": "FULL",
                        "answers": [
                          {"surveyId": 1, "optionType": "A"},
                          {"surveyId": 2, "optionType": "B"},
                          {"surveyId": 3, "optionType": "A"}
                        ]
                      }
                      """
                            ),
                            @ExampleObject(
                                    name = "SubmitQuickWithCode",
                                    summary = "QUICK 제출 예시(Q3 code 포함)",
                                    value = """
                      {
                        "type": "QUICK",
                        "answers": [
                          {"surveyId": 101, "optionType": "C"},
                          {"surveyId": 102, "optionType": "F"},
                          {"surveyId": 103, "optionType": "B", "code": "TAX"},
                        ]
                      }
                      """
                            )
                    }
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "검증 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseDTO.class),
                    examples = @ExampleObject(
                            name = "SubmitSuccess",
                            value = """
                  {
                    "localDateTime": "2025-09-02T10:00:00",
                    "statusCode": 200,
                    "code": "SUCCESS",
                    "message": "설문 검증 성공",
                    "data": {
                      "completed": false,
                      "savedCount": 7
                    }
                  }
                  """
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청(존재하지 않는 문항/옵션 등)",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "SubmitInvalid",
                            value = """
                  {
                    "localDateTime": "2025-09-02T10:00:00",
                    "statusCode": 400,
                    "code": "INVALID_OPTION",
                    "message": "유효하지 않은 옵션입니다.",
                    "data": null
                  }
                  """
                    )
            )
    )
    ResponseDTO<SubmitSurveyResponse> submit(SubmitSurveyRequest request);
}
