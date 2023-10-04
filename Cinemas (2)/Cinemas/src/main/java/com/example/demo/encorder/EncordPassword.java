package com.example.demo.encorder;

import java.security.NoSuchAlgorithmException;

//疎結合のために分離
public interface EncordPassword {
	/**
	 * 引数で受け取ったinputをハッシュ化して返す
	 * @param input
	 * @return inputをハッシュ化」した文字列
	 * @throws NoSuchAlgorithmException
	 */
	String hushString(String input) throws NoSuchAlgorithmException;
	}
