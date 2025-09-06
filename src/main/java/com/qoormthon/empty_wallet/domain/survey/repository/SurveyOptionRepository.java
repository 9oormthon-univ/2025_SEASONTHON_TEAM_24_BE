package com.qoormthon.empty_wallet.domain.survey.repository;

import com.qoormthon.empty_wallet.domain.survey.entity.SurveyOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

// 정렬 -> 같은 질문끼리 묶고(surveyId ASC), 보기 A~F 순(type ASC).
public interface SurveyOptionRepository extends JpaRepository<SurveyOption, Long> {

    // 번들 조회: 여러 질문의 보기를 (질문ID asc → 보기타입 asc)로 정렬
    List<SurveyOption> findBySurveyIdInOrderBySurveyIdAscTypeAsc(Collection<Long> surveyIds);
    Optional<SurveyOption> findBySurveyIdAndType(Long surveyId, String type);

    // 제출 매핑 : (surveyId, type)로 옵션 특정 — QUICK Q1/Q2 등
    List<SurveyOption> findAllBySurveyIdAndType(Long surveyId, String type);
}