package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//入力された投稿内容を受け取る

@Data  //getter&setterの生成
public class CinemasPostForm {
	
	private Integer post_id;
	private String user_id;
	// 何も入力せずにつぶやくと、@NotBlankの動作によってBindingResulにエラー情報を渡される
	@NotBlank(message = "タイトルが登録されていません！")
	// @NotBlank 文字列がnullでないかつ空文字(半角スペースのみ)でないことを検証する。
	private String  title;
	private String impressions;
//	private int rating;
}
