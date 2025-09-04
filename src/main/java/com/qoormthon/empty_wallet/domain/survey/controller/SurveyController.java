package com.qoormthon.empty_wallet.domain.survey.controller;

import com.qoormthon.empty_wallet.domain.survey.docs.SurveyDocs;
import com.qoormthon.empty_wallet.domain.survey.dto.request.SubmitSurveyRequest;
import com.qoormthon.empty_wallet.domain.survey.dto.response.SubmitSurveyResponse;
import com.qoormthon.empty_wallet.domain.survey.dto.response.SurveyBundleResponse;
import com.qoormthon.empty_wallet.domain.survey.entity.SurveyType;
import com.qoormthon.empty_wallet.domain.survey.service.CharacterResolverService;
import com.qoormthon.empty_wallet.domain.survey.service.SurveyCommandService;
import com.qoormthon.empty_wallet.domain.survey.service.SurveyQueryService;
import com.qoormthon.empty_wallet.global.common.dto.response.ResponseDTO;
import com.qoormthon.empty_wallet.global.security.core.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.apache.commons.lang3.StringUtils.firstNonBlank;

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
    private final CharacterResolverService characterResolver;


    // 풀 서베이 번들 조회
    @GetMapping("/full")
    public ResponseDTO<SurveyBundleResponse> getFull() {
        SurveyBundleResponse body = queryService.getSurveyBundle(SurveyType.FULL);
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

        var body = queryService.getSurveyBundle(SurveyType.QUICK, code);
        return ResponseDTO.of(body, "QUICK 설문 조회 성공");
    }

    // 설문 제출(검증 전용, 저장 없음)
    @PostMapping("/responses")
    public ResponseDTO<SubmitSurveyResponse> submit(@RequestBody @Valid SubmitSurveyRequest req) {
        SubmitSurveyResponse body = commandService.submit(req);
        return ResponseDTO.of(body, "설문 검증 성공");
    }

    private static String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) return a;
        if (b != null && !b.isBlank()) return b;
        return null;
    }
}
