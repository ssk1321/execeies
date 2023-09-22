package com.example.demo.repositry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CinemasPost;

/**
 * 複雑なSQLを実行するためのリポジトリ
 * DBに接続
 */
@Repository

public class CinemasPostRepository {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<CinemasPost> findAll() {
		// CINEMAS_POSTテーブルに保存しているレコードをIDの降順で全て取得するSQL文
		String sql = "SELECT USERID, TITLE, IMPRESSIONS FROM CINEMAS_POST ORDER BY POSTID DESC";
		// SQLを十個。戻り値の型は既に定義されているため合わせる。 queryForList() 複数行のレコードを取得
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
		 // リストがなければ新しいリストを作成
		List<CinemasPost> cinemaPostList = new ArrayList<>();
		// 拡張For文で各要素のMapオブジェクトを取り出し。Muttersエンティティに格納→mutterListに格納
		for (Map<String, Object> result : resultList) {
			CinemasPost cinemasPost = new CinemasPost();
			
			// getを取得してキャストした型でCinemasPostにsetする
			cinemasPost.setUserId((String) result.get("UserID"));
			cinemasPost.setTitle((String) result.get("title"));
			cinemasPost.setImpressions((String) result.get("impressions"));
//			cinemasPost.setRating((Integer) result.get("rating"));
			// setしたものをcinemaPostListに追加する
			cinemaPostList.add(cinemasPost);
		}
		return cinemaPostList;
	}
	
	// 入力されたタイトル、感想を取得してresultに格納して渡す
	public int insert(CinemasPost cinemasPost) {
		String sql = "INSERT INTO CINEMAS_POST(, TITLE, IMPRESSIONS) VALUES(?, ?)";
		// 追加されたレコードの数がint型で返却されるので、int型
		int result = jdbcTemplate.update(sql, cinemasPost.getTitle(), cinemasPost.getImpressions());
		return result; // return jdbcTemplate.update(sql, mutter.getName(), mutter.getText())でもよい
		// serviceに戻る
	}

}
