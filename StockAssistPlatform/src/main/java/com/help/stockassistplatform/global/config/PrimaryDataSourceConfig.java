package com.help.stockassistplatform.global.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@Profile("!test")
@EnableTransactionManagement
@EnableJpaRepositories(
	basePackages = {
		"com.help.stockassistplatform.domain.user.repository",
		"com.help.stockassistplatform.domain.report.user.repository",
		"com.help.stockassistplatform.domain.stock.indexed.repository"
	},
	entityManagerFactoryRef = "primaryEntityManagerFactory",
	transactionManagerRef = "primaryTransactionManager"
)
public class PrimaryDataSourceConfig {
	@Value("${spring.datasource.primary.url}")
	private String dbUrl;

	@Value("${spring.datasource.primary.username}")
	private String dbUser;

	@Value("${spring.datasource.primary.password}")
	private String dbPassword;

	@Primary
	@Bean(name = "primaryDataSource")
	public DataSource dataSource() {
		final HikariConfig config = new HikariConfig();
		config.setJdbcUrl(dbUrl);
		config.setUsername(dbUser);
		config.setPassword(dbPassword);
		config.setDriverClassName("com.mysql.cj.jdbc.Driver");
		config.setTransactionIsolation("TRANSACTION_READ_COMMITTED");

		config.setMaximumPoolSize(10);
		config.setMinimumIdle(3);
		config.setIdleTimeout(300_000);
		config.setMaxLifetime(600_000);
		config.setConnectionTimeout(30_000);
		config.setValidationTimeout(5_000);
		config.setKeepaliveTime(300_000);

		return new HikariDataSource(config);
	}

	@Primary
	@Bean(name = "primaryEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
		EntityManagerFactoryBuilder builder,
		@Qualifier("primaryDataSource") DataSource dataSource) {
		final Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		properties.put("hibernate.show_sql", true);
		properties.put("hibernate.hbm2ddl.auto", "none");
		// properties.put("hibernate.physical_naming_strategy",
		// 	"org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");

		return builder
			.dataSource(dataSource)
			.packages(
				"com.help.stockassistplatform.domain.user.entity",
				"com.help.stockassistplatform.domain.report.user.entity",
				"com.help.stockassistplatform.domain.stock.indexed.entity"
			)
			.persistenceUnit("primary")
			.properties(properties)
			.build();
	}

	@Primary
	@Bean(name = "primaryTransactionManager")
	public PlatformTransactionManager transactionManager(
		@Qualifier("primaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
