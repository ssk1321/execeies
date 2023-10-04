package com.example.demo.form;

import lombok.Data;

@Data
// getter&setterの生成
public class SessionForm {
	/** SessionForm*/
	private String user_id;
	/** SessionForm*/
	private String user_name;
	/** SessionForm*/
	private String password;
}
