package com.aaku.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aaku.model.UserDtls;
import com.aaku.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncode;

	@ModelAttribute
	private void userDetails(Model m, Principal p) {
		String email = p.getName();
		UserDtls user = userRepo.findByEmail(email);

		m.addAttribute("user", user);

	}

	@GetMapping("/")
	public String home() {
		return "user/home";
	}
	
	@GetMapping("/changepass")
	public String loadChangePassword() {
		return "user/changepassword";
	}
	
	@PostMapping("/updatePassword")
	public String changePassword(Principal p, @RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass, HttpSession session) {
		String email = p.getName();
		UserDtls loginUser = userRepo.findByEmail(email);
		boolean f = passwordEncode.matches(oldPass, loginUser.getPassword());
		
		if (f) {
			loginUser.setPassword(passwordEncode.encode(newPass));
			UserDtls updateUserPassworDtls = userRepo.save(loginUser);
			
			if (updateUserPassworDtls!=null) {
				session.setAttribute("msg", "Password is Change Successfully");
			} else {
				session.setAttribute("msg", "Something Wrong with Server");
			}
		} else {
			session.setAttribute("msg", "Old Password is Incorrect");
		}
		return "redirect:/user/changepass";
	}
	
	@GetMapping("/editdetails")
	public String editStudentDtls(Model m, Principal p) {
		String email = p.getName();
		UserDtls user = userRepo.findByEmail(email);

		m.addAttribute("student", user);
		return "user/edit_student";
	}
	
	@PostMapping("/updateDetails")
	public String updateStudentDtls( @ModelAttribute("student") UserDtls student, Model m, Principal p) {
		String email = p.getName();
		UserDtls existingStudentDtls = userRepo.findByEmail(email);
		existingStudentDtls.setFullName(student.getFullName());
		existingStudentDtls.setAddress(student.getAddress());
		existingStudentDtls.setMobileNumber(student.getMobileNumber());
		
	
		m.addAttribute("update", userRepo.save(existingStudentDtls));
		return "redirect:/user/";
	}

}

