drop table if exists MEMBER;

create table if not exists MEMBER(
	loginId varchar(12) primary key,
	password varchar(12) not null,
	name varchar(40) not null,
	phone varchar(15),
	email varchar(40)
);