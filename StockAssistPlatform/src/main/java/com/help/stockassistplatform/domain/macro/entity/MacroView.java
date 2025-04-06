package com.help.stockassistplatform.domain.macro.entity;


import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "macroeconomic_vw")
@Immutable
@Getter
public class MacroView {

    @Id
    @Column(name = "crawling_id")
    private String crawlingId;

    @Column(name = "country")
    private String country;

    @Column(name = "index_name")
    private String indexName;

    @Column(name = "index_value")
    private BigDecimal indexValue;

    @Column(name = "posted_at")
    private LocalDateTime postedAt;
}
