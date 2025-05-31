package com.help.stockassistplatform.domain.financial.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "stock_vw")
@Immutable
@Getter
public class StockPriceView {
	@Id
	@Column(name = "ticker")
	private String ticker;

	@Column(name = "name_kr")
	private String name;

	@Column(name = "price")
	private BigDecimal price;
	private Long volume;

	private Float change;
	private BigInteger marketCap;
	@Column(name = "pe")
	private BigDecimal per;
	private BigDecimal eps;
	@Column(name = "dividend_yield")
	private BigDecimal dividend;
}
