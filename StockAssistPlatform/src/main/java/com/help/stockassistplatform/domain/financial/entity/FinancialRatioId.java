package com.help.stockassistplatform.domain.financial.entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FinancialRatioId implements Serializable {

    private String company;
    private LocalDateTime postedAt;
}