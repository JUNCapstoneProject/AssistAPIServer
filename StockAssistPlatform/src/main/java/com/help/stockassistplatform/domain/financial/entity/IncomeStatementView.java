package com.help.stockassistplatform.domain.financial.entity;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "income_statement_vw")
@Immutable
@Getter
public class IncomeStatementView {

    @Id
    @Column(name = "crawling_id")
    private String crawlingId;

    @Column(name = "company")
    private String company;

    @Column(name = "financial_type")
    private String financialType;

    @Column(name = "posted_at")
    private LocalDateTime postedAt;

    @Column(name = "total_revenue")
    private BigDecimal totalRevenue;

    @Column(name = "operating_income")
    private BigDecimal operatingIncome;

    @Column(name = "net_income")
    private BigDecimal netIncome;

    @Column(name = "diluted_eps")
    private BigDecimal eps;

}
