package com.aaku.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aaku.model.UserDtls;
import com.aaku.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncode;

	@Override
	public UserDtls createUser(UserDtls user) {

		user.setPassword(passwordEncode.encode(user.getPassword()));
		user.setRole("ROLE_USER");

		return userRepo.save(user);
	}

	@Override
	public boolean checkEmail(String email) {

		return userRepo.existsByEmail(email);
	}

	@Override
	public List<UserDtls> getAllUser() {
		return userRepo.findAll();
	}

	@Override
	public String getByRole(String role) {
		return userRepo.findByRole(role);
	}

	@Override
	public void deleteStudentById(int id) {
		userRepo.deleteById(id);
		
	}

	@Override
	public UserDtls getStudentById(int id) {
		
		return userRepo.findById(id).get();
		
	}

	
    
}

