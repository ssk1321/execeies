package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data  //getter&setterの生成
public class CinemasPostForm {
	
	private int postId;
	@NotBlank(message = "タイトルを入力してください。")
	// 何も入力せずにつぶやくと、@NotBlankの動作によってBindingResulにエラー情報を渡される
	// @NotBlank 文字列がnullでないかつ空文字(半角スペースのみ)でないことを検証する。
	private String  title;
	private String impressions;
	private int rating;
}
