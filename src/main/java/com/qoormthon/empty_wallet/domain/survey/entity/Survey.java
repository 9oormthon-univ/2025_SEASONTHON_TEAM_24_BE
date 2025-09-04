package com.qoormthon.empty_wallet.domain.survey.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "survey")
@Getter
public class Survey {
    @Id
    @Column(name = "survey_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SurveyType type;   // FULL | QUICK

    @Column(nullable = false, length = 255)
    private String title;
}