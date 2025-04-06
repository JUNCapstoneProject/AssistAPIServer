package com.help.stockassistplatform.domain.financial.entity;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financials_ratio_vw")
@Immutable
@Getter
@IdClass(FinancialRatioId.class)
public class FinancialRatioView {

    @Id
    private String company;

    @Id
    @Column(name = "posted_at")
    private LocalDateTime postedAt;

    @Column(name = "roe")
    private BigDecimal roe;

    @Column(name = "roa")
    private BigDecimal roa;

    @Column(name = "operating_margin")
    private BigDecimal operatingMargin;

    @Column(name = "net_profit_margin")
    private BigDecimal netProfitMargin;

}
