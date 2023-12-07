package com.jdc.database.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jdc.database.dto.Member;

@Repository
public class MemberDao {

	@Autowired
	JdbcTemplate template;
		
	public int create(Member member) {
		return template.update("insert into MEMBER (loginId,password,name,phone,email) values (?,?,?,?,?)", 
					member.getLoginId(),
					member.getPassword(),
					member.getName(),
					member.getPhone(),
					member.getEmail()
				);
	}
}
