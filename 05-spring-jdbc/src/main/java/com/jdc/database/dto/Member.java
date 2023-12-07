package com.jdc.database.dto;

import lombok.Data;

@Data
public class Member {

	String loginId;
	String password;
	String name;
	String phone;
	String email;
}
