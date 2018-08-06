package com.example.user.repository;

import org.springframework.stereotype.Repository;

import com.example.model.Users;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserRepository extends CrudRepository<Users,Integer>{
	Users findByUserEmail(String userEmail);
	
	Users findByUserEmailAndUserPassword(String userEmail,String userPAssword);
	
	List<Users> findAll();
	
	Users save(Users user);
}
