package com.example.demo.service;

import java.security.NoSuchAlgorithmException;

public interface EncorderService {
	/**
	 * 受け取った文字列をSHA-256でハッシュ化して返す
	 * @param input
	 * @return inputをハッシュ化し文字列
	 * @throws NoSuchAlgorithmException
	 */
	String encordPass(String input)throws NoSuchAlgorithmException;

}