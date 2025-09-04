package com.qoormthon.empty_wallet.domain.survey.repository;

import com.qoormthon.empty_wallet.domain.survey.entity.Survey;
import com.qoormthon.empty_wallet.domain.survey.entity.SurveyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 특정 유형(FULL/QUICK)의 모든 질문을 PK 오름차순으로 조회
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByTypeOrderByIdAsc(SurveyType type);
}