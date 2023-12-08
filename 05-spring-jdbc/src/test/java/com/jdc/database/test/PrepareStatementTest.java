package com.jdc.database.test;

import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.jdc.database.dao.rowmap.config.MemberRowMapper;
import com.jdc.database.dto.Member;

@SpringJUnitConfig(locations = "/application.xml")
@TestMethodOrder(OrderAnnotation.class)
public class PrepareStatementTest {
	
	@Autowired
	private JdbcOperations jdbcOperations;
	
	@Autowired
	private MemberRowMapper memberRowMapper;
	
	@Autowired
	@Qualifier("beanRowMapper")
	private RowMapper<Member> beanRowMapper;
	
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
	
	@Test
	@DisplayName("2 preparedStatementCreator")
	@Order(3)
	void testPrepareStatementCreatorWithUpdate(@Qualifier("insertBean") PreparedStatementCreatorFactory creator) {
		
		var preparedStmt = creator.newPreparedStatementCreator(
				List.of("0003","member","MaungMaung","0956789","maungmaung@gmail.com"));
		
		var count = jdbcOperations.update(preparedStmt);
		
		assertEquals(1, count);
	}
	
	@Test
	@DisplayName("3 Select Creator")
	@Order(4)
	void testPreparedStatementCreatorWithSelect(@Qualifier("selectBean") PreparedStatementCreatorFactory creator) {
		
		var preparedStmt = creator.newPreparedStatementCreator(List.of("0001%"));
		
		var count = jdbcOperations.execute(preparedStmt, stmt -> {
			var dataList = new ArrayList<Member>();
			var resultSet= stmt.executeQuery();
			
			while(resultSet.next()) {
				var m = new Member();
				m.setLoginId(resultSet.getString(1));
				m.setPassword(resultSet.getString(2));
				m.setName(resultSet.getString(3));
				m.setPhone(resultSet.getString(4));
				m.setEmail(resultSet.getString(5));
				
				dataList.add(m);
			}
			return dataList;
		});
		
		assertEquals(1, count.size());
	}

	@Test
	@DisplayName("3 Select Creator with query")
	@Order(5)
	void test5(@Qualifier("selectBean") PreparedStatementCreatorFactory creator) {

		var preparedStmt = creator.newPreparedStatementCreator(List.of("0001%"));
		
		var count = jdbcOperations.query(preparedStmt, memberRowMapper);
		
		assertEquals(1, count.size());
	}
	
	@Test
	@DisplayName("4 find by loginId")
	@Order(6)
	void test6(@Qualifier("findByIdBean") PreparedStatementCreatorFactory creator) {
		
		var prepareStmt = creator.newPreparedStatementCreator(List.of("0002"));
		var count = jdbcOperations.query(prepareStmt, res -> {
			while(res.next()) {
				return memberRowMapper.mapRow(res, 1);
			}
			return null;
		});
		assertNotNull(count);
		assertEquals("AungAung", count.getName());
	}
	
	@Test
	@DisplayName("7 execute with pure string")
	@Order(7)
	@Sql(scripts = "/database.sql")
	void test7(@Value("${member.insert}") String sql) {
		var count = jdbcOperations.execute(sql, (PreparedStatement stmt) -> {
			stmt.setString(1, "0001");
			stmt.setString(2, "admin");
			stmt.setString(3, "AdminUser");
			stmt.setString(4, "0956788765");
			stmt.setString(5, "aminuser@mail.com");
			
			return stmt.executeUpdate();
		});
		
		assertEquals(1, count);
	}
	
	@Test
	@DisplayName("8 update with pure string")
	@Order(8)
	@Sql(scripts = "/database.sql")
	void test8(@Value("${member.insert}") String sql) {
		var count = jdbcOperations.update(sql, stmt -> {
			stmt.setString(1, "0001");
			stmt.setString(2, "admin");
			stmt.setString(3, "AdminUser");
			stmt.setString(4, "0956788765");
			stmt.setString(5, "aminuser@mail.com");
		});
		
		assertEquals(1, count);
	}

//	*****easiest usage for update******
	@Test
	@DisplayName("9 update with string and @nullable Object *****easiest usage for update******")
	@Order(9)
	@Sql(scripts = "/database.sql")
	void test9(@Value("${member.insert}") String sql) {
		var count = jdbcOperations.update(sql, "Admin","admin","Admin User","099832743","adminuser@gmail.com");
		
		assertEquals(1, count);
	}

	@Test
	@DisplayName("10 query with pure String and rowMapper")
	@Order(10)
	void test10(@Value("${member.select.find.by.name.like}") String sql) {
		var count = jdbcOperations.query(sql, stmt -> stmt.setString(1, "Adm%"), memberRowMapper);
		assertEquals(1, count.size());
	}

//	**** Good way to use query *****
	@Test
	@DisplayName("11 query with pure String and rowMapper")
	@Order(11)
	void test11(@Value("${member.select.find.by.name.like}") String sql) {
		var count = jdbcOperations.query(sql,memberRowMapper, "Adm%");
		assertEquals(1, count.size());
	}

//	**** Better way to use query *****
	@Test
	@DisplayName("12 query with pure String and BeanPropertyRowMapper")
	@Order(12)
	void test12(@Value("${member.select.find.by.name.like}") String sql) {
		var count = jdbcOperations.query(sql,new BeanPropertyRowMapper<Member>(Member.class), "Adm%");
		assertEquals(1, count.size());
	}

//	**** the best way to use query with bean *****
	@Test
	@DisplayName("13 query with pure String and BeanPropertyRowMapper with bean")
	@Order(13)
	void test13(@Value("${member.select.find.by.name.like}") String sql) {
		var count = jdbcOperations.query(sql,beanRowMapper, "Adm%");
		assertEquals(1, count.size());
	}

	

}










