package com.help.stockassistplatform.domain.financial.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;

@IdClass(StockChartViewId.class)
@Entity
@Table(name = "stock_chart_vw")
@Immutable
@Getter
public class StockChartView {
	@Id
	@Column(name = "ticker")
	private String ticker;

	@Id
	@Column(name = "posted_at")
	private LocalDateTime postedAt;

	@Column(name = "name_kr")
	private String name;

	@Column(name = "adj_close")
	private BigDecimal adjClose;
}
