package com.jdc.database.dao.config;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;

import com.jdc.database.dto.Member;

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
		});
	}
	
	@Bean
	@Qualifier("selectBean")
	public PreparedStatementCreatorFactory preparedStatementCreateorSelect(@Value("${member.select.find.by.name.like}") String sql) {
		return new PreparedStatementCreatorFactory(sql, Types.VARCHAR);
	}

	@Bean
	@Qualifier("findByIdBean")
	public PreparedStatementCreatorFactory prepareStatementCreatorQuery(@Value("${member.select.find.by.loginId}") String sql) {
		return new PreparedStatementCreatorFactory(sql, Types.VARCHAR);
	}
	
	@Bean
	@Qualifier("beanRowMapper")
	public RowMapper<Member> beanRowMapper(){
		return new BeanPropertyRowMapper<Member>(Member.class);
	}
	
}

