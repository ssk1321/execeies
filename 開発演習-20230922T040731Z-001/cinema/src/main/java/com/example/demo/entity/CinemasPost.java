package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity
 * Table name = cinemas_post
 * 
 */
@Data                         // getter&setterを自動生成
@NoArgsConstructor // デフォルトコンストラクタを自動生成します。
@AllArgsConstructor // 全フィールドに対する初期化値を引数にとるコンストラクタを生成します。
public class CinemasPost {
	/**CinemasPost entity*/
	@Id                         // フィールドに付与し、主キーであることを示すアノテーション。
	private Integer postId;
	/**CinemasPost entity*/
    private String userId;
	/**CinemasPost entity*/
	private String title;
	/**CinemasPost entity*/
	private String impressions;
	/**CinemasPost entity*/
	private int rating;

}