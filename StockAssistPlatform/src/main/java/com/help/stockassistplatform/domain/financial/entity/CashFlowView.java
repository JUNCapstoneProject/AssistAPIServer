package com.help.stockassistplatform.domain.financial.entity;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_flow_vw")
@Immutable
@Getter
public class CashFlowView {

    @Id
    @Column(name = "crawling_id")
    private String crawlingId;

    @Column(name = "company")
    private String company;

    @Column(name = "financial_type")
    private String financialType;

    @Column(name = "posted_at")
    private LocalDateTime postedAt;

    @Column(name = "operating_cash_flow")
    private BigDecimal operatingCashFlow;

    @Column(name = "investing_cash_flow")
    private BigDecimal investingCashFlow;

    @Column(name = "financing_cash_flow")
    private BigDecimal financingCashFlow;

    @Column(name = "free_cash_flow")
    private BigDecimal freeCashFlow;

}
