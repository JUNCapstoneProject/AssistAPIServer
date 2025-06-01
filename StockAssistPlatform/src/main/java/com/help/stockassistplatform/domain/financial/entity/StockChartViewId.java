package com.help.stockassistplatform.domain.financial.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockChartViewId implements Serializable {
	private String ticker;
	private LocalDateTime postedAt;
}
