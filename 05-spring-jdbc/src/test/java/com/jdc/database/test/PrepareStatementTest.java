package com.jdc.database.test;

import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(locations = "/application.xml")
@TestMethodOrder(OrderAnnotation.class)
public class PrepareStatementTest {
	
	@Autowired
	JdbcOperations jdbcOperations;
	
	@Test
	@DisplayName("1 prepareStatement test")
	@Order(1)
	@Sql(scripts = "/database.sql")
	void testPrepareStatement(@Value("${member.insert}") String sql) {
		
		var bool = jdbcOperations.execute((Connection conn) -> {
			var stmt = conn.prepareStatement(sql);
			stmt.setString(1, "Member");
			stmt.setString(2, "member");
			stmt.setString(3, "Thida");
			stmt.setString(4, "0956789");
			stmt.setString(5, "thida@gamil.com");
			return stmt;
		}, PreparedStatement::executeUpdate);
		
		assertNotNull(bool);
	}
}
