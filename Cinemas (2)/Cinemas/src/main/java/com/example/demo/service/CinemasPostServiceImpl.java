package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.RetentionUser;
import com.example.demo.repositry.CinemasPostRepository;

@Service
public class CinemasPostServiceImpl implements CinemasPostService {

	@Autowired // CinemasPostRepositoryをnewする
	CinemasPostRepository cinemasPostRepository;
	
	@Override
	// 全ユーザーの投稿の一覧表示
	public List<RetentionUser> findAllPost() {
		// TODO 自動生成されたメソッド・スタブ
			// CustomMuttersRepositoryのfindAll()メソッドを実行
			List<RetentionUser> allUserPostList = cinemasPostRepository.findAll();
			return allUserPostList;

	}
	
	@Override
	// ユーザー自身の投稿のみを取得
	public List<RetentionUser> findPostsBySelf(String user_id) {
		// TODO 自動生成されたメソッド・スタブ
		List<RetentionUser> mySelfPostList = cinemasPostRepository.FindByUserSelf(user_id);
		return mySelfPostList;
	}

	@Override
	// post_idとtitle、impressionsが格納されたRetentionUserが引数
	public boolean createNewPost(RetentionUser retentionUser) {
		// TODO 自動生成されたメソッド・スタブ
			int result = cinemasPostRepository.insert(retentionUser);
			// 投稿が追加されたかされないかの条件分岐
			if (result == 1) {
				// resultに１が入っていたら、投稿が成功
				return true;
			}
			return false;
		}
	
	@Override
	  // ユーザーが指定したpost_idに一致する投稿を削除
    public int deletePost(Integer post_id) {
        return cinemasPostRepository.deletePost(post_id);
    }
	
    // 映画投稿の評価を処理するメソッドの実装
	@Override
	public void rateCinemaPost(Integer post_id, Integer rating) {
	    // 映画投稿の評価をデータベースに保存する処理を追加
	    cinemasPostRepository.rateCinemaPost(post_id, rating);
	}
	
    // タイトルで映画投稿を検索するメソッドの実装
    @Override
    public List<RetentionUser> searchByTitle(String title) {
        return cinemasPostRepository.searchByTitle(title);
    }
}
