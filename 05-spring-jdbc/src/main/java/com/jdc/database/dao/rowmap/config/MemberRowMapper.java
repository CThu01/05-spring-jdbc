package com.jdc.database.dao.rowmap.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.jdc.database.dao.meta.MemberRow;
import com.jdc.database.dto.Member;

@MemberRow
public class MemberRowMapper implements RowMapper<Member>{

	@Override
	public Member mapRow(ResultSet res, int rowNum) throws SQLException {
		var m = new Member();
		m.setLoginId(res.getString(1));
		m.setPassword(res.getString(2));
		m.setName(res.getString(3));
		m.setPhone(res.getString(4));
		m.setEmail(res.getString(5));
		return m;
	}

}
