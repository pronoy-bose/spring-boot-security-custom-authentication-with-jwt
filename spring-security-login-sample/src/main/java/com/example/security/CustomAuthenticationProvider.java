package com.example.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.user.VO.LoginVO;
import com.example.user.VO.UserVO;
import com.example.user.service.UserService;

import java.util.ArrayList;

import org.springframework.security.authentication.BadCredentialsException;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private UserService userService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserVO userVO;
		String userEmail = authentication.getName();
		String userPassword = authentication.getCredentials().toString();

		LoginVO loginVO = new LoginVO();
		loginVO.setUserEmail(userEmail);
		loginVO.setUserPassword(userPassword);
		try {
			userVO = userService.findByUserEmailAndUserPassword(loginVO);
		} catch (Exception e) {
			logger.error("Authentication failed for user = " + userEmail);
			throw new BadCredentialsException("Authentication failed for user = " + userEmail);
		}

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userEmail, userPassword,
				new ArrayList<>());

		logger.info("Succesful Authentication with user = " + userEmail);
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}