package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.RetentionUser;

public interface CinemasPostService {
	/**
	 * cinema_postテーブルから全てのユーザー投稿を取得する
	 * @Param なし
	 * @return Iterable<CinemasPost>
	 */
	
	List<RetentionUser> findAllPost();
	
	/**
	 * 新しい投稿内容をDBに保存する
	 * @Param RetentionUser
	 * @return boolean
	 */
	
	boolean createNewPost(RetentionUser retentionUser);
	
	/**
	 * cinema_postテーブルからユーザー自身の投稿内容だけを全て取得する
	 * @Param String user_id
	 * @return Iterable<CinemasPost>
	 */
	List<RetentionUser> findPostsBySelf(String user_id);
	
	/**
	 * ユーザー自身が投稿したものを削除する
	 * @Param Integer postId
	 * @return int
	 */
	int deletePost(Integer post_id);
	
/**
 * 映画投稿の評価を処理するメソッド
 * @param Integer post_id, Integer rating
 * @return void
 */ 
	void rateCinemaPost(Integer post_id, Integer rating);
	
	/**
	 * タイトルで映画投稿を検索するメソッドを追加
	 * @param String query
	 * @return List<RetentionUser>
	 */ 
	List<RetentionUser> searchByTitle(String title);

}
