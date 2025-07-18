/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.batch.item.database.support;

import javax.sql.DataSource;

import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

/**
 * @author Henning Pöttker
 */
@Testcontainers(disabledWithoutDocker = true)
@SpringJUnitConfig
@Sql(scripts = "query-provider-fixture.sql", executionPhase = BEFORE_TEST_CLASS)
class MariaDBPagingQueryProviderIntegrationTests extends AbstractPagingQueryProviderIntegrationTests {

	// TODO find the best way to externalize and manage image versions
	private static final DockerImageName MARIADB_IMAGE = DockerImageName.parse("mariadb:11.8.2");

	@Container
	public static MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>(MARIADB_IMAGE);

	MariaDBPagingQueryProviderIntegrationTests(@Autowired DataSource dataSource) {
		super(dataSource, new MySqlPagingQueryProvider());
	}

	@Configuration
	static class TestConfiguration {

		@Bean
		public DataSource dataSource() throws Exception {
			MariaDbDataSource datasource = new MariaDbDataSource();
			datasource.setUrl(mariaDBContainer.getJdbcUrl());
			datasource.setUser(mariaDBContainer.getUsername());
			datasource.setPassword(mariaDBContainer.getPassword());
			return datasource;
		}

	}

}
