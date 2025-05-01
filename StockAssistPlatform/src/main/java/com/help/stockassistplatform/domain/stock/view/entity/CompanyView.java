package com.help.stockassistplatform.domain.stock.view.entity;

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
public class CompanyView {
	@Id
	@Column(name = "company_id")
	private Integer companyId;

	@Column(name = "ticker")
	private String ticker;

	@Column(name = "name_kr")
	private String companyNameKr;

	@Column(name = "name_en")
	private String companyNameEn;
}
