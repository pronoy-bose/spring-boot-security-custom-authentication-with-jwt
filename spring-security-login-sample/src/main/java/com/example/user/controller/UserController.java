package com.example.user.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Users;
import com.example.user.VO.LoginVO;
import com.example.user.VO.UserVO;
import com.example.user.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping(path = "/getAllUsers")
	public ResponseEntity<List<Users>> getAllUsers() {
		List<Users> users = userService.findAll();

		return new ResponseEntity<List<Users>>(users, HttpStatus.OK);
	}

	@PostMapping(path = "/getByUserEmail")
	public ResponseEntity<UserVO> getByUserEmail(@RequestBody String userEmail) {
		UserVO user;
		try {
			user = userService.findByUserEmail(userEmail);
			return new ResponseEntity<UserVO>(user, HttpStatus.OK);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<UserVO>(HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			return new ResponseEntity<UserVO>(HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(path = "/registerUser")
	public ResponseEntity<HashMap<String, String>> registerUser(@RequestBody Users user) {
		try {
			HashMap<String, String> saved = userService.save(user);
			if (saved.get("response").equals("registered")) {
				return new ResponseEntity<HashMap<String, String>>(saved, HttpStatus.OK);
			} else if (saved.get("response").equals("exist")) {
				return new ResponseEntity<HashMap<String, String>>(saved, HttpStatus.OK);
			} else {
				return new ResponseEntity<HashMap<String, String>>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<HashMap<String, String>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
