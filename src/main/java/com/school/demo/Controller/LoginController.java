package com.school.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.school.demo.request.JoinRequest;
import com.school.demo.service.UserService;

@Controller
public class LoginController {
	
	private UserService userService;
	
	@Autowired
	public LoginController (UserService userService) {
		this.userService = userService;
		}
	
    @GetMapping("/login")
    public String MainPage() {
        return "login";
        }
    
    @GetMapping("/join")
    	public String JoinPage() {
    		return "join";
    	} 
        
    @PostMapping("/join")
    	public String Join(@ModelAttribute JoinRequest requestBody) {
    	
    	String username = requestBody.getUsername();
    	String password = requestBody.getPassword();
    	String name = requestBody.getName();
    	String phoneNumber = requestBody.getPhoneNumber();
    	
    	if(userService.joinUser(username, password, name, phoneNumber)>0) {
    		return "redirect:/login";
    	}
    	else return "redirect:/join";
    }
}