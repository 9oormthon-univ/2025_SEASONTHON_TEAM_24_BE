package com.qoormthon.empty_wallet.domain.survey.service;

import com.qoormthon.empty_wallet.domain.survey.dto.request.SubmitSurveyRequest;
import com.qoormthon.empty_wallet.domain.survey.dto.response.SubmitSurveyResponse;
import com.qoormthon.empty_wallet.domain.survey.entity.Survey;
import com.qoormthon.empty_wallet.domain.survey.entity.SurveyOption;
import com.qoormthon.empty_wallet.domain.survey.entity.SurveyType;
import com.qoormthon.empty_wallet.domain.survey.repository.SurveyOptionRepository;
import com.qoormthon.empty_wallet.domain.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

// 설문 제출(검증) 서비스 - 스테이트리스 버전.
// 서버는 아무 것도 저장하지 않는다.
// 이번 요청으로 넘어온 답들을 검증해 savedCount/complete 여부만 계산해 준다.
@Service
@RequiredArgsConstructor
public class SurveyCommandService {

    private final SurveyRepository surveyRepo;
    private final SurveyOptionRepository optionRepo;

    @Transactional(readOnly = true)
    public SubmitSurveyResponse submit(SubmitSurveyRequest req) {
        // 1) 해당 타입(FULL/QUICK)의 "필수 문항" 목록 확보
        List<Survey> questions = surveyRepo.findByTypeOrderByIdAsc(req.type());
        if (questions.isEmpty()) {
            // 질문이 없으면 완료로 볼 수 없음
            return new SubmitSurveyResponse(false, 0);
        }
        Set<Long> requiredIds = questions.stream()
                .map(Survey::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        int requiredCount = requiredIds.size();

        // 2) 모든 옵션을 한 번에 로딩해 매핑 테이블 구성 (N+1 방지)
        List<SurveyOption> allOptions = optionRepo.findBySurveyIdInOrderBySurveyIdAscTypeAsc(requiredIds);

        // (a) (surveyId, type) -> 옵션 리스트 (동일 type이 여러 개면 QUICK Q3같이 code로 구분)
        Map<Long, Map<String, List<SurveyOption>>> bySurveyType = new LinkedHashMap<>();
        for (SurveyOption o : allOptions) {
            bySurveyType
                    .computeIfAbsent(o.getSurveyId(), k -> new LinkedHashMap<>())
                    .computeIfAbsent(o.getType(), k -> new ArrayList<>())
                    .add(o);
        }

        // (b) 빠른 조회를 위한 맵: (surveyId|type|code) -> optionId
        Map<String, Long> bySurveyTypeCode = new HashMap<>();
        for (SurveyOption o : allOptions) {
            String key = key(o.getSurveyId(), o.getType(), nullToEmpty(o.getCode()));
            bySurveyTypeCode.put(key, o.getId());
        }

        // 3) 요청 정리: 같은 surveyId의 마지막 선택만 인정
        Map<Long, SubmitSurveyRequest.Answer> lastBySurvey = new LinkedHashMap<>();
        if (req.answers() != null) {
            for (var a : req.answers()) {
                if (a == null || a.surveyId() == null || a.optionType() == null) continue;
                if (!requiredIds.contains(a.surveyId())) continue; // 현재 타입의 문항이 아니면 무시
                lastBySurvey.put(a.surveyId(), a);
            }
        }

        // 4) 각 답에 대해 "유효한 보기"인지 검증
        int accepted = 0;
        Set<Long> answeredSurveyIds = new HashSet<>();

        for (var entry : lastBySurvey.entrySet()) {
            Long surveyId = entry.getKey();
            var a = entry.getValue();
            String optType = a.optionType();
            String code = a.code(); // QUICK Q3 같은 케이스에서 필요

            List<SurveyOption> candidates = bySurveyType
                    .getOrDefault(surveyId, Map.of())
                    .getOrDefault(optType, List.of());

            if (candidates.isEmpty()) {
                // 존재하지 않는 보기(type) → 무시
                continue;
            }

            Long optionId = null;

            if (candidates.size() == 1) {
                // (surveyId, type)이 유일 → code 없이도 단일 매칭
                optionId = candidates.get(0).getId();
            } else {
                // 동일 (surveyId, type)이 여러 개 → code로 구분 필수
                if (code == null || code.isBlank()) {
                    // 모호 → 무시 (혹은 예외로 바꿔도 됨)
                    continue;
                }
                optionId = bySurveyTypeCode.get(key(surveyId, optType, code));
            }

            if (optionId != null) {
                accepted++;
                answeredSurveyIds.add(surveyId);
            }
        }

        // 5) 완료 판정: 유효하게 답한 "서베이ID의 개수"가 전체 문항 수와 같으면 완료
        boolean completed = (answeredSurveyIds.size() == requiredCount);

        return new SubmitSurveyResponse(completed, accepted);
    }

    private static String key(Long surveyId, String type, String code) {
        return surveyId + "|" + type + "|" + (code == null ? "" : code);
    }

    private static String nullToEmpty(String s) {
        return (s == null) ? "" : s;
    }
}
