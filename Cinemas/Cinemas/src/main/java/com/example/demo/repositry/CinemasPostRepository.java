package com.example.demo.repositry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.RetentionUser;

/**
 * 複雑なSQLを実行するためのリポジトリ
 * DBに接続
 */
@Repository // 忘れるとサーバーが起動しない
public class CinemasPostRepository {
	@Autowired
	JdbcTemplate jdbcTemplate;

	// 全ユーザーの投稿を一覧にする
	public List<RetentionUser> findAll() {
		// CINEMAS_POSTテーブルに保存しているレコードをIDの降順で全て取得するSQL文
		String sql = "SELECT a.USER_NAME, cp.USER_ID, cp.POST_ID, cp.TITLE, cp.IMPRESSIONS, cp.RATING "
				+ "FROM cinemas_post cp INNER JOIN account a ON cp.user_id = a.user_id";
		// SQLを実行。戻り値の型は既に定義されているため合わせる。 queryForList() 複数行のレコードを取得
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
		List<RetentionUser> allUserPostList = new ArrayList<>(); // リストがなければ新しいリストを作成
		// 拡張For文で各要素のMapオブジェクトを取り出し。CinemasPostエンティティに格納→cinemaPostListに格納
		for (Map<String, Object> result : resultList) {
			RetentionUser retentionUser = new RetentionUser();

			// getを取得してキャストした型でRetentionUserにsetする
			retentionUser.setPost_id((Integer) result.get("post_id"));
			retentionUser.setUser_id((String) result.get("user_id"));
			retentionUser.setUser_name((String) result.get("user_name"));
			retentionUser.setTitle((String) result.get("title"));
			retentionUser.setImpressions((String) result.get("impressions"));
			retentionUser.setRating((Integer) result.get("rating"));
//			 setしたものをcinemaPostListに追加する
			allUserPostList.add(retentionUser);
		}
		return allUserPostList;
	}

	// 指定されたユーザーIDに一致する投稿をデータベースから取得しています
	// 取得された投稿はリストとして返されます。このメソッドを呼び出すことで、ログインユーザーの投稿一覧を取得できます
	public List<RetentionUser> FindByUserSelf(String user_id) {
		// ログインしたユーザーの情報を取得。accountテーブルとcinemas_postテーブルの内部結合SQL文
		String sql = "SELECT a.USER_NAME, cp.USER_ID, cp.POST_ID, cp.TITLE, cp.IMPRESSIONS, cp.RATING "
				+ "FROM cinemas_post cp INNER JOIN account a ON cp.user_id = a.user_id WHERE a.user_id = ? ";
		// SQLを実行。戻り値は合わせる。queryForList() 複数行のレコードを取得
		List<Map<String, Object>> myResultList = jdbcTemplate.queryForList(sql, user_id);
		List<RetentionUser> mySelfPostList = new ArrayList<>(); // リストがなければ新しいリストを作成
		// 拡張For文で各要素のMapオブジェクトを取り出し。retentionUserエンティティに格納→mySelfPostListに格納
		for (Map<String, Object> result : myResultList) {
			RetentionUser retentionUser = new RetentionUser();      // Thymeleafで使うretentionUser

			// getを取得してキャストした型でretentionUseにsetする
			retentionUser.setPost_id((Integer) result.get("Post_id"));
			retentionUser.setUser_id((String) result.get("user_id"));
			retentionUser.setUser_name((String) result.get("user_name"));
			retentionUser.setTitle((String) result.get("title"));
			retentionUser.setImpressions((String) result.get("impressions"));
			retentionUser.setRating((Integer) result.get("rating"));
			// setしたものをmySelfPostListに追加する
			mySelfPostList.add(retentionUser);
		}
		return mySelfPostList;
	}

	// 投稿内容を取得してDBに追加する。resultに格納して渡す
	public int insert(RetentionUser retentionUser) {
		String sql = "INSERT INTO CINEMAS_POST(USER_ID, TITLE, IMPRESSIONS,RATING) VALUES( ?, ?, ?, ?)";
		// 追加されたレコードの数がint型で返却されるので、int型
		int result = jdbcTemplate.update(sql, retentionUser.getUser_id(), retentionUser.getTitle(),retentionUser.getImpressions(), retentionUser.getRating());
		// return jdbcTemplate.update(sql, cinemasPost.getUser_id(), cinemasPost.getTitle(),cinemasPost.getImpressions())でもよい
		return result; // serviceに戻る
	}
	
	 //投稿を削除するメソッド。 post_idに一致する
    public int deletePost(Integer post_id) {
        String sql = "DELETE FROM CINEMAS_POST WHERE post_id = ? ";
        return jdbcTemplate.update(sql, post_id);
    }
    
    // 映画投稿の評価をデータベースに保存するメソッドの実装
    public void rateCinemaPost(Integer post_id, Integer rating) {
        String sql = "UPDATE cinemas_post SET rating = ? WHERE post_id = ?";
        jdbcTemplate.update(sql, rating, post_id);
    }
    
 // タイトルで映画投稿を検索するメソッドの実装
    public List<RetentionUser> searchByTitle(String title) {
        String sql = "SELECT a.user_name, cp.title, cp.impressions, cp.rating "
        		+ "FROM cinemas_post cp INNER JOIN account a ON cp.user_id = a.user_id WHERE title LIKE ?";
        String searchText = "%" + title + "%";
        return jdbcTemplate.query(sql, new Object[] { searchText }, new BeanPropertyRowMapper<>(RetentionUser.class));
    }
    
}
