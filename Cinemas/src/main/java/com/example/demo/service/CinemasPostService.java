package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.CinemasPost;

public interface CinemasPostService {
	/**
	 * cinema_postテーブルから全てのユーザー投稿を取得する
	 * @Param なし
	 * @return Iterable<CinemasPost>
	 */
	
	List<CinemasPost> findAllPost();
	
	/**
	 * 新しい投稿内容をDBに保存する
	 * @Param CinemasPost
	 * @return boolean
	 */
	
	boolean createNewPost(CinemasPost cinemasPost);
	
	/**
	 * cinema_postテーブルからユーザー自身の投稿内容だけを全て取得する
	 * @Param 
	 * @return Iterable<CinemasPost>
	 */
	List<CinemasPost> findMyPost();
}
