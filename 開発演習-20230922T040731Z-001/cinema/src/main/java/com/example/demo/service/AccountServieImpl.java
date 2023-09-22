package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Account;
import com.example.demo.form.SessionForm;
import com.example.demo.repositry.AccountRepository;

@Service
public class AccountServieImpl implements AccountService {
	@Autowired
	AccountRepository accountRepository;

	@Override
	public String loginCheck(SessionForm loginUser) {
		// TODO 自動生成されたメソッド・スタブ
		String userName = accountRepository.findAccount(loginUser);
		return userName;
	}

	@Override
	public boolean registerCheck(Account registerUser) {
		// TODO 自動生成されたメソッド・スタブ
		return accountRepository.findAccount(registerUser);
	}

	@Override
	public boolean createNewAccount(Account account) {
		// TODO 自動生成されたメソッド・スタブ
		int result = accountRepository.insert(account);
		if (result == 1) {
			return true;
		}
		return false;
	}

}
