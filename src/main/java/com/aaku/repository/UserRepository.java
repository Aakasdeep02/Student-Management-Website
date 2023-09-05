package com.aaku.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aaku.model.UserDtls;
import java.util.List;


public interface UserRepository extends JpaRepository<UserDtls, Integer> {

	public boolean existsByEmail(String email);

	public UserDtls findByEmail(String email);
	
	public UserDtls findByEmailAndMobileNumber(String email, String mobileNumber);
	
    public String findByRole(String role);
}
