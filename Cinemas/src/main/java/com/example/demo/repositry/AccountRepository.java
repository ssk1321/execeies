package com.example.demo.repositry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.RetentionUser;
import com.example.demo.form.SessionForm;

@Repository
public class AccountRepository {
	@Autowired
	JdbcTemplate jdbcTemplate;

	// ログインフォームに入力したuser_idとpassが、DBに存在するかを確認するメソッド
	// DBに該当するレコードがあった場合、そのレコードのuser_name列を返す
	public String findAccount(SessionForm loginUser) {
		String sql = "SELECT USER_ID, USER_NAME, PASSWORD, NAME, MAIL FROM ACCOUNT WHERE USER_ID = ? AND PASSWORD = ?";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, loginUser.getUser_id(),
				loginUser.getPassword());
		// 一致するレコードがなかった場合はresultLisut = [];になってしまう。実際はnullではない
		if (resultList != null) {
			for (Map<String, Object> result : resultList) {                       // resultLisut = [];の場合はスキップする
				if (loginUser.getUser_id().equals((String) result.get("user_id"))
						&& loginUser.getPassword().equals((String) result.get("password"))) {
					return (String) result.get("user_name");                       // sessionFormのオブジェクトのuser_nameフィールドがまだ空なので、この値をreturn
				}
			}
		}
		return null;
	}

	// DBに格納されているaccountの情報を照合するメソッド
	public boolean findAccount(Account registerUser) {
		// 引数で受け取ったregisterUserオブジェクトとuser_id,user_name,passが重複するレコードが何件あるか検索するSQL
		String sql = "SELECT COUNT(*) FROM ACCOUNT WHERE USER_ID = ? AND USER_NAME = ? AND PASSWORD = ?";
		int result = jdbcTemplate.queryForObject(sql, Integer.class, registerUser.getUser_id(),
				registerUser.getUser_name(), registerUser.getPassword());   // 重複するレコードの件数が返却される
		if (result == 0) {
			return true;  // 引数で受け取ったregiterUserと同じuser_id,passを持つレコードがDBに存在しない
		}
		return false;     // 存在する
	}

	// // 引数で受け取ったAccount entityの情報をDBに追加するメソッド
	public int insert(Account account) {
		String sql = "INSERT INTO ACCOUNT(USER_ID, USER_NAME, PASSWORD, NAME, MAIL) VALUES(?, ?, ?, ?, ?)";
		// insertできたレコードの件数がint型で返却される。一件追加するSQLのため、成功→１件、失敗→０件
		int result = jdbcTemplate.update(sql, account.getUser_id(), account.getUser_name(),
				account.getName(), account.getPassword(), account.getMail());
		return result;
	}

	// アカウント情報を保持しておくくメソッド
	public List<RetentionUser> getRetentionUser() {
		// accountテーブルとcinemas_postテーブルを内部結合させてそれぞれのカラムを持ってくるSQL文
			String sql = "SELECT c.USER_ID, a.USER_NAME, c.POST_ID, c.TITLE, c.IMPRESSIONS "
					+ "FROM ACCOUNT as a INNER JOIN CINEMAS_POST as c ON a.USER_ID = c.USER_ID";
			  List<Map<String, Object>> UserResult = jdbcTemplate.queryForList(sql);  	// queryForList(sql)で複数の結果を取得する
			    List<RetentionUser> resultUserList = new ArrayList<>();                             // ユーザー情報を取得して、Userオブジェクトに新しいリストを作成
		 // Mapで得たresult
			for (Map<String, Object> result : UserResult) {                                                // 拡張For文で取得。各要素のオブジェクトを取り出し。User entityに格納→userListに格納
				RetentionUser retentionUser = new RetentionUser();         
				
				retentionUser.setUser_id((String) result.get("user_id"));                            // getを取得してキャストした型でCinemasPostにsetする
				System.out.println((String) result.get("user_id"));
				retentionUser.setUser_name((String) result.get("user_name")); 
				System.out.println((String) result.get("user_name"));
				retentionUser.setPost_id((Integer) result.get("post_id")); 
				retentionUser.setTitle((String) result.get("title")); 
				retentionUser.setImpressions((String) result.get("impression")); 

				resultUserList.addAll(resultUserList);                                                          // setしたものをcinemaPostListに追加する
			}
			return resultUserList;
	}
}
