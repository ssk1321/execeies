package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity
 * Table name = account
 */

@Data                         // getter&setterを自動生成
@NoArgsConstructor // デフォルトコンストラクタを自動生成します。
@AllArgsConstructor // 全フィールドに対する初期化値を引数にとるコンストラクタを生成します。

public class Account {
	/** Account */
	@Id                         // フィールドに付与し、主キーであることを示すアノテーション。
	private String user_id;
	
	/** Account */
	private String user_name;
	/** Account */
	private String password;
	/** Account */
	private String name;
	/** Account */
	private String mail;
	
}
