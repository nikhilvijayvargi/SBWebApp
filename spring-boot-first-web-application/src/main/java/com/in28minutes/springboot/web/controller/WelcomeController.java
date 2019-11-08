package com.in28minutes.springboot.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

//import com.in28minutes.springboot.web.service.LoginService;

@Controller
@SessionAttributes("name")
// we have renamed the LoginController to Welcome Controller now
// We can get the Logged in user name from either @SessionAttribute or Spring Security implemented
// We are even Commenting out @LoginService as we are not using it anymore
public class WelcomeController {

	//@Autowired
	//LoginService loginService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showWelcomePage(ModelMap model) {
		model.put("name", getLoggedinUserName());
		//model.put("name", "nikhil");
		return "welcome";
	}
	
	
	private String getLoggedinUserName() {
		// User in Spring is called as Prinicpal
		Object principal = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		// UserDetails is an Inbuilt Spring class
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}
		
		return principal.toString();
	}

//	@RequestMapping(value = "/login", method = RequestMethod.GET)
//	// @ResponseBody
//	public String showLoginPage(ModelMap modelMap) {
//		return "login";
//	}
//
//	@RequestMapping(value = "/login", method = RequestMethod.POST)
//	public String showWelcomePage(ModelMap model, @RequestParam String name, @RequestParam String password) {
//		boolean isValidUser = loginService.validateUser(name, password);
//
//		if (!isValidUser) {
//			model.put("errorMessage", "Invalid Credentials");
//			return "login";
//		} else {
//
//			model.put("name", name);
//			model.put("password", password);
//
//			return "welcome";
//		}
//	}
}
