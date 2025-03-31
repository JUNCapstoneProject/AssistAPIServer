package com.help.stockassistplatform;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.help.stockassistplatform.global.config.TestPrimaryDataSourceConfig;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestPrimaryDataSourceConfig.class)
class StockAssistPlatformApplicationTests {

	@Test
	void contextLoads() {
	}
}
