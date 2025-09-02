package com.qoormthon.empty_wallet.domain.survey.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "`option`")
@Getter
public class SurveyOption {
    @Id @Column(name = "option_id")
    private Long id;

    @Column(name = "survey_id", nullable = false)
    private Long surveyId;

    @Column(nullable = false, length = 1)
    private String type;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 32)
    private String code;

    @Column(nullable = false)
    private Integer weight;
}