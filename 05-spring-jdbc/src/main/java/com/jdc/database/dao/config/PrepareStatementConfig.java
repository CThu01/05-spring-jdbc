package com.jdc.database.dao.config;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;

@Configuration
public class PrepareStatementConfig {

	@Bean
	@Qualifier("insertBean")
	public PreparedStatementCreatorFactory prepareStatementCreatorFactor(@Value("${member.insert}") String sql) {
		return new PreparedStatementCreatorFactory( sql, new int[] {
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR
		}
			);
	}
}
