package com.qoormthon.empty_wallet.domain.survey.repository;

import com.qoormthon.empty_wallet.domain.survey.entity.SurveyOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

// 정렬 -> 같은 질문끼리 묶고(surveyId ASC), 보기 A~F 순(type ASC).
public interface SurveyOptionRepository extends JpaRepository<SurveyOption, Long> {
    List<SurveyOption> findBySurveyIdInOrderBySurveyIdAscTypeAsc(Collection<Long> surveyIds);
}