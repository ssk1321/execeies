package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CinemasPost;
import com.example.demo.repositry.CinemasPostRepository;

@Service
public class CinemasPostServiceImpl implements CinemasPostService {
	
	@Autowired // CustomMuttersRepositoryをnewする
	CinemasPostRepository cinemasPostRepository;
	
	@Override
	public List<CinemasPost> findMyPost() {
		// TODO 自動生成されたメソッド・スタブ
		// cinema_postテーブルからユーザー自身の投稿のみを取得
		List<CinemasPost> cinemasMyPost = cinemasPostRepository.findAll();
		return cinemasMyPost;
	}

	
	@Override
	public List<CinemasPost> findAllPost() {
		// TODO 自動生成されたメソッド・スタブ
		// cinema_postテーブルから全件取得
			// CustomMuttersRepositoryのfindAll()メソッドを実行
			List<CinemasPost> cinemasPost = cinemasPostRepository.findAll();
			return cinemasPost;
	}

	@Override
	// post_idとtitle、impressionsが格納されたcinemasPostが引数
	public boolean createNewPost(CinemasPost cinemasPost) {
		// TODO 自動生成されたメソッド・スタブ
			int result = cinemasPostRepository.insert(cinemasPost);
			// 投稿が追加されたかされないかの条件分岐
			if (result == 1) {
				// resultに１が入っていたら、投稿が成功
				return true;
			}
			return false;
		}

}
