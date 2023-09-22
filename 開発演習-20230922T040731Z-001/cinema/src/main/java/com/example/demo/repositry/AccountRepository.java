package com.example.demo.repositry;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Account;
import com.example.demo.form.SessionForm;

@Repository
public class AccountRepository {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
		// ログインフォームに入力したuser_idとpassが、DBに存在するかを確認するメソッド
		// DBに該当するレコードがあった場合、そのレコードのuser_name列を返す
		public String findAccount(SessionForm loginUser) {
			String sql = "SELECT USERID, USERNAME, PASSWORD, NAME, MAIL FROM ACCOUNT WHERE USERID = ? AND PASSWORD = ?";
			List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, loginUser.getUserId(), loginUser.getPassword());
			// 一致するレコードがなかった場合はresultLisut = [];になってしまう。実際はnullではない。
			
			if (resultList != null) {
				for (Map<String, Object> result : resultList) {  // resultLisut = [];の場合はスキップする
					if (loginUser.getUserId().equals((String) result.get("userId")) && loginUser.getPassword().equals((String) result.get("password"))) {
						return (String)result.get("userName");         // sessionFormのオブジェクトのuser_nameフィールドがまだ空なので、この値をreturn
					}
				}
			}
			return null;
		}
		
		public boolean findAccount(Account registerUser) { // 新規登録フォームで入力した５つの項目が格納されている
			 // 引数で受け取ったregisterUserオブジェクトとuser_id,user_name,passが重複するレコードが何件あるか検索するSQL
			String sql = "SELECT COUNT(*) FROM ACCOUNT WHERE USERID = ? AND USERNAME = ? AND PASSWORD = ?";
			// 重複するレコードの件数が返却される
			int result = jdbcTemplate.queryForObject(sql, Integer.class, registerUser.getUserId(), registerUser.getUserName(), registerUser.getPassword());
			if (result == 0) {
				return true; // 引数で受け取ったregiterUserと同じuser_id,passを持つレコードがDBに存在しない
			}
			return false;   // 存在する
		}
		
		public int insert(Account account) {
			// 引数で受け取ったAccounts entityの情報をDBに保存
			String sql = "INSERT INTO ACCOUNT(USERID, USERNAME, PASSWORD, NAME, MAIL) VALUES(?, ?, ?, ?, ?)";
			// insertできたレコードの件数がint型で返却される
			// 一見追加するSQLのため、成功→１、失敗→０
			int result = jdbcTemplate.update(sql, account.getUserId(), account.getUserName(),
					account.getName(), account.getPassword(), account.getMail());
			return result;
		}

}
