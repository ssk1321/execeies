package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// 入力されたユーザー情報を受け取る

@Data
public class AccountForm {
	/**
	 * 必須チェックのためのアノテーション。
	 * NULL、空文字、半角空白のみ、タブのみの場合NGになる。ただし全角スペースはパスする。
	 */
	@NotBlank(message = "ユーザーIDが入力されていません。")
	private String user_id;
	@NotBlank(message = "ユーザー名が入力されていません。")
	private String user_name;
	@NotBlank(message = "パスワードが入力されていません。")
	private String password;
	@NotBlank(message = "名前が入力されていません。")
	private String name;
	@NotBlank(message = "mailが入力されていません。")
	private String mail;
}
