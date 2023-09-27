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
@Repository // 忘れるとサーバーが起動しない
public class CinemasPostRepository {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<CinemasPost> findAll() {
		// CINEMAS_POSTテーブルに保存しているレコードをIDの降順で全て取得するSQL文
		String sql = "SELECT POST_ID, USER_ID , TITLE, IMPRESSIONS FROM CINEMAS_POST ORDER BY POST_ID DESC";
		// SQLを実行。戻り値の型は既に定義されているため合わせる。 queryForList() 複数行のレコードを取得
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
		// リストがなければ新しいリストを作成
		List<CinemasPost> cinemaPostList = new ArrayList<>();
		// 拡張For文で各要素のMapオブジェクトを取り出し。Muttersエンティティに格納→mutterListに格納
		for (Map<String, Object> result : resultList) {
			CinemasPost cinemasPost = new CinemasPost();

			// getを取得してキャストした型でCinemasPostにsetする
			cinemasPost.setPost_id((Integer) result.get("post_id"));
			cinemasPost.setUser_id((String) result.get("user_id"));
			cinemasPost.setTitle((String) result.get("title"));
			cinemasPost.setImpressions((String) result.get("impressions"));
			// cinemasPost.setRating((Integer) result.get("rating"));
			// setしたものをcinemaPostListに追加する
			cinemaPostList.add(cinemasPost);
		}
		return cinemaPostList;
	}

	// 指定されたユーザーID（listUserId）に一致する投稿をデータベースから取得しています。
	// 取得された投稿はリストとして返されます。このメソッドを呼び出すことで、ログインユーザーの投稿一覧を取得できます。
	public List<CinemasPost>MySelfFindAll() {
		// accountテーブルとcinemas_postテーブルの内部結合SQL文
//		String sql = "SELECT USER_ID, USER_ID FROM ACCOUNT INNER JOIN CINEMAS_POST ON ACCOUNT.USER_ID = CINEMAS_POST.USER_ID";
		String sql = "SELECT * FROM CINEMAS_POST WHERE user_id == ?";
		// SQLを実行。戻り値は合わせる。queryForList() 複数行のレコードを取得
		List<Map<String, Object>> mySelfResultList = jdbcTemplate.queryForList(sql);
		// リストがなければ新しいリストを作成
		List<CinemasPost> mySelfPostList = new ArrayList<>();
		// 拡張For文で各要素のMapオブジェクトを取り出し。Muttersエンティティに格納→mutterListに格納
				for (Map<String, Object> result :  mySelfResultList) {
					CinemasPost cinemasPost = new CinemasPost();
					
					// getを取得してキャストした型でmutterにsetする
					cinemasPost.setPost_id((Integer) result.get("Post_id"));
					cinemasPost.setUser_id((String) result.get("user_id"));
					cinemasPost.setTitle((String) result.get("title"));
					cinemasPost.setImpressions((String) result.get("impressions"));
					// setしたものをmutterListに追加する
					mySelfPostList.add(cinemasPost);
				}
		return mySelfPostList;
	}

	// 入力されたタイトル、感想を取得してresultに格納して渡す
	public int insert(CinemasPost cinemasPost) {
		String sql = "INSERT INTO CINEMAS_POST(USER_ID, TITLE, IMPRESSIONS) VALUES( ?, ?, ?)";
		// 追加されたレコードの数がint型で返却されるので、int型
		System.out.println(cinemasPost.getUser_id());
		System.out.println(cinemasPost.getTitle());
		System.out.println(cinemasPost.getUser_id());
		int result = jdbcTemplate.update(sql, cinemasPost.getUser_id(), cinemasPost.getTitle(),
				cinemasPost.getImpressions());
		return result; // return jdbcTemplate.update(sql, mutter.getName(), mutter.getText())でもよい
		// serviceに戻る
	}
}
