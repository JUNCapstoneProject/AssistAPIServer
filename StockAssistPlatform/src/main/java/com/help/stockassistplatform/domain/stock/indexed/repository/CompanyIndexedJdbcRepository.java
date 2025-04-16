package com.help.stockassistplatform.domain.stock.indexed.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.help.stockassistplatform.domain.stock.indexed.entity.CompanyIndexed;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CompanyIndexedJdbcRepository {

	private final JdbcTemplate jdbcTemplate;

	public void batchInsert(final List<CompanyIndexed> companies) {
		final String sql = """
				INSERT INTO company_indexed (company_id, ticker, name_kr, name_en)
				VALUES (?, ?, ?, ?)
			""";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(final PreparedStatement ps, final int i) throws SQLException {
				final CompanyIndexed c = companies.get(i);
				ps.setInt(1, c.getCompanyId());
				ps.setString(2, c.getTicker());
				ps.setString(3, c.getCompanyNameKr());
				ps.setString(4, c.getCompanyNameEn());
			}

			@Override
			public int getBatchSize() {
				return companies.size();
			}
		});
	}
}

