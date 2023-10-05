package com.example.demo.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.encorder.EncordPassword;

@Service // つけ忘れるとサーバーが起動しないので注意
public class EncorderServiceImple implements EncorderService {
	
	@Autowired
	EncordPassword encorder;

	@Override
	public String encordPass(String input) {
		// エンコードされたパスをnullで定義
		String encordedPass = null;
		try {
			// 引数で受け取ったinputをハッシュ化して返すhushStringメソッドを実行
			encordedPass = encorder.hushString(input);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			// エラーメッセージ追加して別ページに飛ばす処理などもあり
		}
		// TODO 自動生成されたメソッド・スタブ
		return encordedPass;
		// tryブロックが失敗した場合はNullのまま
	}

}