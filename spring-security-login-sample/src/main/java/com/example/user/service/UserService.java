package com.example.user.service;

import java.util.HashMap;
import java.util.List;

import com.example.model.Users;
import com.example.user.VO.LoginVO;
import com.example.user.VO.UserVO;

public interface UserService {

	List<Users> findAll();
	
	UserVO findByUserEmailAndUserPassword(LoginVO loginVO) throws NullPointerException, Exception;

	UserVO findByUserEmail(String userEmail);
	
	HashMap<String, String> save(Users user);
}
