package com.qoormthon.empty_wallet.domain.survey.controller;

import com.qoormthon.empty_wallet.domain.survey.docs.SurveyDocs;
import com.qoormthon.empty_wallet.domain.survey.dto.request.SubmitSurveyRequest;
import com.qoormthon.empty_wallet.domain.survey.dto.response.SubmitSurveyResponse;
import com.qoormthon.empty_wallet.domain.survey.dto.response.SurveyBundleResponse;
import com.qoormthon.empty_wallet.domain.survey.entity.SurveyType;
import com.qoormthon.empty_wallet.domain.survey.service.SurveyCommandService;
import com.qoormthon.empty_wallet.domain.survey.service.SurveyQueryService;
import com.qoormthon.empty_wallet.global.common.dto.response.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 설문 컨트롤러 (스테이트리스)
 *
 * - GET /api/surveys/full  : FULL 설문 번들(질문 title + 보기 type/title)
 * - GET /api/surveys/quick : QUICK 설문 번들
 * - POST /api/surveys/responses : 설문 제출(검증만 수행, 저장 없음) → completed/savedCount 반환
 *
 * 공통 응답 포맷(ResponseDTO)으로 래핑해 내려준다.
 */
@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController implements SurveyDocs {

    private final SurveyQueryService queryService;
    private final SurveyCommandService commandService;


    // 풀 서베이 번들 조회
    @GetMapping("/full")
    public ResponseDTO<SurveyBundleResponse> getFull() {
        SurveyBundleResponse body = queryService.getSurveyBundle(SurveyType.FULL);
        return ResponseDTO.of(body, "FULL 설문 조회 성공");
    }

    // 퀵 서베이 번들 조회
    @GetMapping("/quick")
    public ResponseDTO<SurveyBundleResponse> getQuick() {
        SurveyBundleResponse body = queryService.getSurveyBundle(SurveyType.QUICK);
        return ResponseDTO.of(body, "QUICK 설문 조회 성공");
    }

    // 설문 제출(검증 전용, 저장 없음)
    @PostMapping("/responses")
    public ResponseDTO<SubmitSurveyResponse> submit(@RequestBody @Valid SubmitSurveyRequest req) {
        SubmitSurveyResponse body = commandService.submit(req);
        return ResponseDTO.of(body, "설문 검증 성공");
    }
}
