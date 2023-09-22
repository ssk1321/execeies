package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.CinemasPost;

public interface CinemasPostService {
	/**
	 * Muttersテーブルから全てのつぶやきを取得する
	 * @Param なし
	 * @return Iterable<CinemasPost>
	 */
	
	List<CinemasPost> findAllPost();
	
	/**
	 * 新しいつぶやき内容をDBに保存する
	 * @Param mutter
	 * @return boolean
	 */
	
	boolean createNewPost(CinemasPost cinemasPost);

}
