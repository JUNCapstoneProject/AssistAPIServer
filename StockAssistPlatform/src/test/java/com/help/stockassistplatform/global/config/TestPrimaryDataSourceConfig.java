package com.help.stockassistplatform.global.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

@TestConfiguration
@EnableTransactionManagement
@EnableJpaRepositories(
	basePackages = "com.help.stockassistplatform.domain.user.repository",
	entityManagerFactoryRef = "testEntityManagerFactory",
	transactionManagerRef = "testTransactionManager"
)
public class TestPrimaryDataSourceConfig {

	@Bean(name = "testDataSource")
	public DataSource testDataSource() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUsername("sa");
		dataSource.setPassword("");
		return dataSource;
	}

	@Bean(name = "testEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
		@Qualifier("testDataSource") DataSource dataSource,
		EntityManagerFactoryBuilder builder) {
		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.put("hibernate.show_sql", true);

		return builder
			.dataSource(dataSource)
			.packages("com.help.stockassistplatform.domain.user.entity")
			.persistenceUnit("test")
			.properties(properties)
			.build();
	}

	@Bean(name = "testTransactionManager")
	public PlatformTransactionManager transactionManager(
		@Qualifier("testEntityManagerFactory") EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}

	@Bean
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
		return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
	}
}
