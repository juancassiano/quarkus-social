CREATE DATABASE quarkus_social;

CREATE TABLE USERS (
	id bigserial not null primary key,
	name varchar(100) not null,
	age integer not null
	
)

SELECT * FROM USERS

CREATE TABLE POSTS (
	id bigserial not null primary key,
	post_text varchar(150) not null,
	dateTime timestamp not null,
	user_id bigint not null,
	foreign key (user_id) references USERS(id)
)

SELECT * FROM POSTS


CREATE TABLE FOLLOWERS (
	id bigserial not null primary key,
	follower_id bigint not null,
	user_id bigint not null,
	foreign key (follower_id) references USERS(id),
	foreign key (user_id) references USERS(id)
)

SELECT * FROM FOLLOWERS