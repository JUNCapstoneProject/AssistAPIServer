package com.help.stockassistplatform.domain.financial.entity;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "balance_sheet_vw")
@Immutable
@Getter
public class BalanceSheetView {

    @Id
    @Column(name = "crawling_id")
    private String crawlingId;

    @Column(name = "company")
    private String company;

    @Column(name = "financial_type")
    private String financialType;

    @Column(name = "posted_at")
    private LocalDateTime postedAt;

    @Column(name = "total_assets")
    private BigDecimal totalAssets;

    @Column(name = "total_liabilities")
    private BigDecimal totalLiabilities;

    @Column(name = "stockholders_equity")
    private BigDecimal stockholdersEquity;

    @Column(name = "debt_ratio")
    private BigDecimal debtRatio;

}
