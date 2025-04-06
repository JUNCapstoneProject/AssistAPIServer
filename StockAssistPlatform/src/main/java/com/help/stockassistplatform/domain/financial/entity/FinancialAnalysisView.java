package com.help.stockassistplatform.domain.financial.entity;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financials_analysis_vw")
@Immutable
@Getter
@IdClass(FinancialAnalysisId.class)
public class FinancialAnalysisView {

    @Id
    private String company;

    @Id
    @Column(name = "posted_at")
    private LocalDateTime postedAt;

    @Column(name = "ai_analysis")
    private String aiAnalysis;

}
