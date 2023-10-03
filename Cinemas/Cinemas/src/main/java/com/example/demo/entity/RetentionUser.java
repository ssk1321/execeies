package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity
 * ユーザーのID,USER_NAMEを保存しておく
 * */

@Data                         // getter&setterを自動生成
@NoArgsConstructor // デフォルトコンストラクタを自動生成します。
@AllArgsConstructor // 全フィールドに対する初期化値を引数にとるコンストラクタを生成します。

public class RetentionUser {
	/** RetentionUser*/
	private String user_id;
	/** RetentionUser*/
	private String user_name;
	/** RetentionUser*/
	private Integer post_id;
	/** RetentionUser*/
	private String title;
	/** RetentionUser*/
	private String impressions;
	/** RetentionUser*/
	private Integer rating;
}
