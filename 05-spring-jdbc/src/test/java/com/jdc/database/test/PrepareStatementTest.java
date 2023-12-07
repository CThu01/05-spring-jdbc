package com.jdc.database.test;

import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
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
			stmt.setString(1, "0001");
			stmt.setString(2, "member");
			stmt.setString(3, "Thida");
			stmt.setString(4, "0956789");
			stmt.setString(5, "thida@gamil.com");
			return stmt;
		}, PreparedStatement::executeUpdate);
		
		assertNotNull(bool);
	}
	
	@Test
	@DisplayName("2 preparedStatementCreator")
	@Order(2)
	void testPrepareStatementCreator(@Qualifier("insertBean") PreparedStatementCreatorFactory creator) {
		
//		var creator =  new PreparedStatementCreatorFactory(sql,
//				Types.VARCHAR,
//				Types.VARCHAR,
//				Types.VARCHAR,
//				Types.VARCHAR,
//				Types.VARCHAR
//			);
		
		var preparedStmt = creator.newPreparedStatementCreator(List.of("0002","member","AungAung","0956789","aungaung@gmail.com"));
		
		var count = jdbcOperations.execute(preparedStmt, PreparedStatement::executeUpdate);
		
		assertEquals(1, count);
	}
	
	
	
	
	
	
}
















