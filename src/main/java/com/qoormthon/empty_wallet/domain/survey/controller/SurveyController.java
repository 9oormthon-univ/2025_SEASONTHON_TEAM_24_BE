package com.qoormthon.empty_wallet.domain.survey.controller;

import com.qoormthon.empty_wallet.domain.survey.docs.SurveyDocs;
import com.qoormthon.empty_wallet.domain.survey.dto.request.SubmitSurveyRequest;
import com.qoormthon.empty_wallet.domain.survey.dto.response.SubmitSurveyResponse;
import com.qoormthon.empty_wallet.domain.survey.dto.response.SurveyBundleResponse;
import com.qoormthon.empty_wallet.domain.survey.entity.SurveyType;
import com.qoormthon.empty_wallet.domain.survey.service.CharacterResolverService;
import com.qoormthon.empty_wallet.domain.survey.service.SurveyCommandService;
import com.qoormthon.empty_wallet.domain.survey.service.SurveyService;
import com.qoormthon.empty_wallet.global.common.dto.response.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    private final SurveyService surveyService;
    private final SurveyCommandService commandService;
    private final CharacterResolverService characterResolver;


    // 풀 서베이 번들 조회
    @GetMapping("/full")
    public ResponseDTO<SurveyBundleResponse> getFull() {
        SurveyBundleResponse body = surveyService.getSurveyBundle(SurveyType.FULL);
        return ResponseDTO.of(body, "FULL 설문 조회 성공");
    }

    // 수정 (권장)
    @GetMapping("/quick")
    @Override
    public ResponseDTO<SurveyBundleResponse> getQuick(
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        // 혹시 보안 설정이 잘못되어 비인증 접근이 들어오면 방어
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        // 프론트에서 character 안 받으니 param/header는 null로, 유저ID로 캐릭터 코드 결정
        String code = characterResolver.resolve(null, userId);

        var body = surveyService.getSurveyBundle(SurveyType.QUICK, code);
        return ResponseDTO.of(body, "QUICK 설문 조회 성공");
    }

        // 설문 제출(검증 전용, 저장 없음)
        @PostMapping("/responses")
        @Override
        public ResponseDTO<SubmitSurveyResponse> submit(
                @AuthenticationPrincipal(expression = "id") Long userId,  // ← userId → id
                @Valid @RequestBody SubmitSurveyRequest request
        ) {
            if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            var res = surveyService.submit(userId, request);
            return ResponseDTO.of(res, "설문 검증 성공");
        }
}
