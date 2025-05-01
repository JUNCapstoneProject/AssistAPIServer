package com.help.stockassistplatform.global.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
	basePackages = {
		"com.help.stockassistplatform.domain.news.repository",
		"com.help.stockassistplatform.domain.report.expert.repository",
		"com.help.stockassistplatform.domain.stock.view.repository",
		"com.help.stockassistplatform.domain.financial.repository",
		"com.help.stockassistplatform.domain.macro.repository"
	},
	entityManagerFactoryRef = "secondaryEntityManagerFactory",
	transactionManagerRef = "secondaryTransactionManager"
)
public class SecondaryDataSourceConfig {
	@Value("${spring.datasource.secondary.url}")
	private String dbUrl;

	@Value("${spring.datasource.secondary.username}")
	private String dbUser;

	@Value("${spring.datasource.secondary.password}")
	private String dbPassword;

	@Bean(name = "secondaryDataSource")
	public DataSource dataSource() {
		final HikariConfig config = new HikariConfig();
		config.setJdbcUrl(dbUrl);
		config.setUsername(dbUser);
		config.setPassword(dbPassword);
		config.setDriverClassName("com.mysql.cj.jdbc.Driver");
		config.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
		return new HikariDataSource(config);
	}

	@Bean(name = "secondaryEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
		EntityManagerFactoryBuilder builder,
		@Qualifier("secondaryDataSource") DataSource dataSource) {
		final Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect"); // Dialect 명시적 설정
		properties.put("hibernate.show_sql", true);
		properties.put("hibernate.generate_statistics", true);
		properties.put("hibernate.hbm2ddl.auto", "none"); // 읽기 전용 DB이므로

		return builder
			.dataSource(dataSource)
			.packages(
				"com.help.stockassistplatform.domain.news.entity",
				"com.help.stockassistplatform.domain.report.expert.entity",
				"com.help.stockassistplatform.domain.stock.view.entity",
				"com.help.stockassistplatform.domain.financial.entity",
				"com.help.stockassistplatform.domain.macro.entity"
			)
			.persistenceUnit("secondary")
			.properties(properties) // Hibernate 설정 추가
			.build();
	}

	@Bean(name = "secondaryTransactionManager")
	public PlatformTransactionManager transactionManager(
		@Qualifier("secondaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
