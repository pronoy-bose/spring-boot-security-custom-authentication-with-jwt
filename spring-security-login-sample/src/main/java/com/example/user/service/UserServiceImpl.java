package com.example.user.service;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Users;
import com.example.user.VO.LoginVO;
import com.example.user.VO.UserVO;
import com.example.user.mapper.UserMapper;
import com.example.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserVO findByUserEmail(String userEmail) {
		// TODO Auto-generated method stub
		Users findUser =  userRepository.findByUserEmail(userEmail);
		UserMapper mapUser = new UserMapper();
		UserVO userVO = mapUser.toUserVO(findUser);
		return userVO;
	}

	@Override
	public List<Users> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public UserVO findByUserEmailAndUserPassword(LoginVO loginVO) throws NullPointerException,Exception {
		// TODO Auto-generated method stub
		Users findUser = userRepository.findByUserEmail(loginVO.getUserEmail());
		String hashedPassword = hashString(loginVO.getUserPassword(),findUser.getUserVerificationCode()); 
		Users user = userRepository.findByUserEmailAndUserPassword(loginVO.getUserEmail(), hashedPassword);
		UserMapper mapUser = new UserMapper();
		UserVO userVO = mapUser.toUserVO(user);
		return userVO;
	}

	@Override
	public HashMap<String, String> save(Users user) {
		// TODO Auto-generated method stub
		HashMap<String, String> responseMap = new HashMap<String, String>();
		try {
			Users queryUser = userRepository.findByUserEmail(user.getUserEmail());
			if (queryUser != null) {
				responseMap.put("response", "exist");
				return responseMap;
			} else {
				String salt = getRandomKey();
				String hashedPassword = hashString(user.getUserPassword(),salt);
				user.setUserVerificationCode(salt);
				user.setUserPassword(hashedPassword);
				userRepository.save(user);
				responseMap.put("response", "registered");
				return responseMap;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			responseMap.put("response", "error");
			return responseMap;
		}
	}
	
	private static String getRandomKey(){
		return UUID.randomUUID().toString();
	}

	private static String hashString(String password,String salt) throws Exception {

		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashedBytes = digest.digest((password+salt).getBytes("UTF-8"));

		return convertByteArrayToHexString(hashedBytes);

	}
	
	private static String convertByteArrayToHexString(byte[] arrayBytes) {
	    StringBuffer stringBuffer = new StringBuffer();
	    for (int i = 0; i < arrayBytes.length; i++) {
	        stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
	                .substring(1));
	    }
	    return stringBuffer.toString();
	}

}
