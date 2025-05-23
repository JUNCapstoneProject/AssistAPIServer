package com.help.stockassistplatform.domain.financial.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

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
	@Column(name = "crawling_id")
	private String crawlingId;

	@Column(name = "posted_at") // ← 실제 DB 컬럼 이름이 이거일 가능성 높음. 확인 필요!
	private LocalDateTime postedAt;

	@Column(name = "name_kr")
	private String name;

	private String ticker;

	private BigDecimal open;
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal close;
	private Long volume;

	private Float change;
	private BigInteger marketCap;
	private BigDecimal per;
	private BigDecimal eps;
	@Column(name = "dividend_yield")
	private BigDecimal dividend;
}
