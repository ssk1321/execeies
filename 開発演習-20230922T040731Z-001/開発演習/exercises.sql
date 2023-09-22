create table account (
	user_id serial primary key not null,
	user_name character varying(20) not null,
	password character varying(10) not null,
	name character varying(10) not null,
	mail character varying(30)not null );

create table cinema_mutter (
	mutter_id serial not null,
	user_id character varying(20) not null,
	user_name character varying(10) not null,
	title character varying(100) not null,
	impressions text not null );

insert into account(user_id, user_name, password, name, mail)
values (1, 'ssk', '12345', '佐々木', 'sukkiriShop@gmail.jp');

