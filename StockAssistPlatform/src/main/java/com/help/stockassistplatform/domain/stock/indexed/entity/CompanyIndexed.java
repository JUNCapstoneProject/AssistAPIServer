package com.help.stockassistplatform.domain.stock.indexed.entity;

import com.help.stockassistplatform.domain.stock.view.entity.CompanyView;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CompanyIndexed {
	@Id
	@Column(name = "company_id")
	private Integer companyId;

	@Column(name = "ticker")
	private String ticker;

	@Column(name = "name_kr")
	private String companyNameKr;

	@Column(name = "name_en")
	private String companyNameEn;

	public static CompanyIndexed from(final CompanyView viewData) {
		return new CompanyIndexed(
			viewData.getCompanyId(),
			viewData.getTicker(),
			viewData.getCompanyNameKr(),
			viewData.getCompanyNameEn()
		);
	}
}
