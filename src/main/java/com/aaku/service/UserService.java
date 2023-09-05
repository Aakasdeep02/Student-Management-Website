package com.aaku.service;

import java.util.List;

import com.aaku.model.UserDtls;

public interface UserService {

	public UserDtls createUser(UserDtls user);

	public boolean checkEmail(String email);
	
	public List<UserDtls> getAllUser();
	
   public String getByRole(String role);
   
   public void deleteStudentById(int id);
   
   public UserDtls getStudentById(int id);

}
