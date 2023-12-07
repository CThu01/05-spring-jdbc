package com.jdc.database.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.jdc.database.config.ApplicationConfig;
import com.jdc.database.dao.MemberDao;
import com.jdc.database.dto.Member;

//@SpringJUnitConfig(locations = "/application.xml")
@SpringJUnitConfig(classes = ApplicationConfig.class)
@TestMethodOrder(OrderAnnotation.class)
public class MemberTest {

	@Autowired
	MemberDao memberDao;
	
	@Autowired
	JdbcOperations jdbcOperation;
	
	@Test
	@Sql(scripts = "/database.sql")
	@Order(1)
	void testCreate() {
		var member = new Member();
		member.setLoginId("admin");
		member.setPassword("admin");
		member.setName("Admin");
		
		memberDao.create(member);
	}
	
	@Test
	@Order(2)
	void testConnectionCreate() {
		
		var result = jdbcOperation.execute((Connection conn) -> {
			
			var stmt = conn.createStatement();
			var resultSet = stmt.executeQuery("select count(*) from member");
			while(resultSet.next()) {
				return resultSet.getLong(1);
			}
			return 0;
		});
		
		assertEquals(1l, result);
	}
	
	@Test
	@Order(3)
	void testStmtCreate() {
		var result = jdbcOperation.execute((Statement stmt) -> {
			var resultSet = stmt.executeQuery("select count(*) from member");
			while(resultSet.next()) {
				return resultSet.getLong(1);
			}
			return 0;
		});
		
		assertEquals(1l, result);
	}
	
	@Test
	@Order(4)
	void testQueryResultSetExecutor() {
		
		jdbcOperation.execute("""
				insert into member (loginId,password,name) values ('Member','member','Thidar')
				""");
		
		var result = jdbcOperation.query("select * from member", res -> {
			var dataList = new ArrayList<Member>();
			while(res.next()) {
				var m = new Member();
				m.setLoginId(res.getString(1));
				m.setPassword(res.getString(2));
				m.setName(res.getString(3));
				m.setPhone(res.getString(4));
				m.setEmail(res.getString(5));
				
				dataList.add(m);
			}
			
			return dataList;
		});
		
		assertEquals(2, result.size());
	}
	
	@Test
	@Order(5)
	void testQueryRollCallBackHandler() {
		
		var dataList = new ArrayList<Member>();
		jdbcOperation.query("select * from member", res -> {
			var m = new Member();
			m.setLoginId(res.getString(1));
			m.setPassword(res.getString(2));
			m.setName(res.getString(3));
			m.setPhone(res.getString(4));
			m.setEmail(res.getString(5));
			dataList.add(m);
		});
			
		assertEquals(2, dataList.size());
	}
	
	@Test
	@Order(6)
	void testQueryRowMapper() {
		var dataList = jdbcOperation.query("select * from member", 
				(res,rono) -> {
					var m = new Member();
					m.setLoginId(res.getString(1));
					m.setPassword(res.getString(2));
					m.setName(res.getString(3));
					m.setPhone(res.getString(4));
					m.setEmail(res.getString(5));
					return m;
				});
		assertEquals(2, dataList.size());
	}
	
	@Test
	@Order(7)
	void testQueryForObject() {
		
		var mapResult = jdbcOperation.queryForMap("select * from member where loginId = 'admin'");
		
		assertEquals("admin", mapResult.get("loginId"));
	}
	
	
	
	
}









