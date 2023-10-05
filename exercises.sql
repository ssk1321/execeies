変更前
create table account (
	user_id serial primary key not null,
	user_name character varying(20) not null,
	password character varying(10) not null,
	name character varying(10) not null,
	mail character varying(30)not null );

変更後 serialだと自動連番のためIDが確認できないので、character型にする。
create table account (
	user_id character varying(20) primary key not null,
	user_name character varying(20) not null,
	password character varying(10) not null,
	name character varying(10) not null,
	mail character varying(30)not null );

変更前
create table cinema_mutter (
	mutter_id serial not null,
	user_id character varying(20) not null,
	user_name character varying(20) not null,
	title character varying(100) not null,
	impressions text not null );

変更後	primary key 追加
		foreign key(外部キーにするカラム) references 親テーブル名(同じカラム);
create table cinemas_post (
	post_id serial primary key not null,
	user_id character varying(20) not null,
	title character varying(100) not null,
	impressions text not null,
	rating integer(5) not null,
	foreign key(user_id) references account(user_id)
	);

追加したテスト情報
1,insert into account(user_id, user_name, password, name, mail)
values ('ssk', 'ssk1', '12345', '佐々木', 'sukkiriShop@gmail.jp');

2,insert into account(user_id, user_name, password, name, mail)
values ('mnt', 'minato', '1234', '湊 雄輔', 'sukkiriShop@gmail.jp');


insert into cinemas_post(post_id, user_id, title, impressions)
values (34, 'ssk', '羊たちの沈黙', 'レクター博士役の人がとてもよかった。');

インナージョイン
SELECT c.USER_ID, a.USER_NAME,  FROM ACCOUNT as a INNER JOIN CINEMAS_POST as c ON c.USER_ID = 'ssk';

accountテーブルとcinemas_postテーブルを内部結合させてそれぞれのカラムを持ってくるSQL文
SELECT c.USER_ID, a.USER_NAME, c.POST_ID, c.TITLE, c.IMPRESSIONS
				FROM ACCOUNT as a INNER JOIN CINEMAS_POST as c ON a.USER_ID = c.USER_ID VALUES(?, ?, ?, ?, ?);

SELECT c.USER_ID, a.USER_NAME, c.POST_ID, c.TITLE, c.IMPRESSIONS
				FROM ACCOUNT as a INNER JOIN CINEMAS_POST as c ON a.USER_ID = c.USER_ID

List<CinemasPost> mySelfPostList =  cinemasPostService.findMyPost();
model.addAttribute("mySelfPostList", mySelfPostList);