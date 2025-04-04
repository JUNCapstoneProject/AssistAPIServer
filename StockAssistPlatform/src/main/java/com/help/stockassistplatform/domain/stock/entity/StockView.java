package com.help.stockassistplatform.domain.stock.entity;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company_vw")
@Immutable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockView {
	@Id
	@Column(name = "company_id")
	private Integer companyId;

	@Column(name = "ticker", nullable = false)
	private String ticker;

	@Column(name = "company_name_kr", nullable = false)
	private String companyNameKr;
}
