package com.jdc.database.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

@Configuration
@PropertySource(value = { "/database.properties" })
@ComponentScan(basePackages = {"com.jdc.database.dao"})
public class ApplicationConfig {

	@Value("${db.url}")
	String url;
	@Value("${db.usr}")
	String user;
	@Value("${db.pwd}")
	String password;
	
	@Bean
	public MysqlConnectionPoolDataSource dataSource() {
		
		var dataSource = new MysqlConnectionPoolDataSource();
		dataSource.setUrl(url);
		dataSource.setUser(user);
		dataSource.setPassword(password);
		return dataSource;
	}
	
	@Bean
	public JdbcTemplate jabcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource,true);
	}
	
	
	
	
	
	
}











