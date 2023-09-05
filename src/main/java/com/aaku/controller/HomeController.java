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
import org.springframework.web.bind.annotation.RequestParam;

import com.aaku.model.UserDtls;
import com.aaku.repository.UserRepository;
import com.aaku.service.UserService;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@ModelAttribute
	private void userDetails(Model m, Principal p) {
		if(p!=null) {
			String email = p.getName();
			UserDtls user = userRepo.findByEmail(email);
            m.addAttribute("user", user);
		}
	}

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/signin")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@PostMapping("/createUser")
	public String createuser(@ModelAttribute UserDtls user, HttpSession session) {

		// System.out.println(user);

		boolean f = userService.checkEmail(user.getEmail());

		if (f) {
			session.setAttribute("msg", "Email Id alreday exists");
		}

		else {
			UserDtls userDtls = userService.createUser(user);
			if (userDtls != null) {
				session.setAttribute("msg", "Register Sucessfully");
			} else {
				session.setAttribute("msg", "Something wrong on server");
			}
		}

		return "redirect:/register";
	}
	
	@GetMapping("/loadForgotPassword")
	public String loadForgotPassword() {
		return "forgotpassword";
	}
	
	@GetMapping("/loadResetPassword/{id}")
	public String loadResetPassword(@PathVariable int id, Model m) {
		m.addAttribute("id", id);
		return "resetpassword";
	}
	
	@PostMapping("/forgotPassword")
	public String forgotPassword(@RequestParam String email, @RequestParam String mobileNum, HttpSession session) {
		UserDtls user = userRepo.findByEmailAndMobileNumber(email, mobileNum);
		
		if(user!=null) {
			session.setAttribute("msg", "Email and Mobile Number Correct");
			return "redirect:/loadResetPassword/" + user.getId();
		} else {
			session.setAttribute("msg", "Invalid Email and Mobile Number");
			return "forgotpassword";
		}
	}
	
	@PostMapping("/changePassword")
	public String resetPassword(@RequestParam String psw, @RequestParam Integer id, HttpSession session) {
		UserDtls user = userRepo.findById(id).get();
		
		String encryptPsw = passwordEncoder.encode(psw);
		user.setPassword(encryptPsw);
		
		UserDtls updateUserDtls = userRepo.save(user);
		
		if (updateUserDtls!=null) {
			session.setAttribute("msg", "Password Change Successfully");
		}
		
		return "redirect:/loadForgotPassword";
	}

}