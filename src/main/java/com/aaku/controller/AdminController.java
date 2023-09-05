package com.aaku.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aaku.model.UserDtls;
import com.aaku.repository.UserRepository;
import com.aaku.service.UserService;


@Controller
@RequestMapping("/admin")
public class AdminController {
    
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserService userService;
	
	@ModelAttribute
	private void userDetails(Model m, Principal p) {
		String email = p.getName();
		UserDtls user = userRepo.findByEmail(email);

		m.addAttribute("user", user);

	}
	
	@ModelAttribute
	private void studentDetails(Model m) {
		m.addAttribute("students", userService.getAllUser());
	}	
	
	@GetMapping("/") 
	public String home() {
		return "admin/home";
	}
	
	@GetMapping("/{id}")
	public String deleteById(@PathVariable int id) {
		
		userService.deleteStudentById(id);
		
		return "redirect:/admin/";
	}
	
	@GetMapping("/studentdetail/{id}")
	public String studentDetail(@PathVariable int id, Model m) {
	
		m.addAttribute("student", userService.getStudentById(id)); 
		
		return "admin/student_detail";
	}

	
}
