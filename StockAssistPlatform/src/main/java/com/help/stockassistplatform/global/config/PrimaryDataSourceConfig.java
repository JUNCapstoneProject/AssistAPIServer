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
	basePackages = "com.help.stockassistplatform.domain.user.repository",
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
		properties.put("hibernate.hbm2ddl.auto", "update");

		return builder
			.dataSource(dataSource)
			.packages("com.help.stockassistplatform.domain.user.entity")
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
