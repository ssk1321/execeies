package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Account;
import com.example.demo.entity.RetentionUser;
import com.example.demo.form.SessionForm;

@Service
public interface AccountService {
	/**
	 * ログインフォームに入力されたuser_idとpassがDBに存在するか確認
	 * @param userLogin
	 * @return boolean
	 */
	String loginCheck(SessionForm loginUser);
	
	/**
	 * ログインフォームに入力されたuser_idとpassがDBに存在するか確認(Accounts型を受け取る)
	 * @param userLogin
	 * @return boolean
	 */
	// repogitoryのfindAccount()メソッドをそのままreturn
	boolean registerCheck(Account registerUser);
	
	/**
	 * 新規会員登録フォームに入力されたデータをDBに保存
	 * @param account
	 * @return boolean
	 */
	boolean createNewAccount(Account account);

/**
 * ユーザーのid,nameなどの情報を保持
 * @param なし
 * @return List<RetentionUser>
  */
	List<RetentionUser> saveUser();

}
