package com.school.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.school.demo.ErrorCode.ResultCode;
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
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "사용자 정보가 틀려 로그인에 실패했습니다. 다시 시도해주세요.");
            model.addAttribute("errorCode", 401);
            return "errorPage";
        }
        return "login"; // 로그인 페이지의 뷰 이름을 반환합니다.
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
    	
    	if(userService.joinUser(username, password, name, phoneNumber)==ResultCode.SUCCESS) {
    		return "redirect:/login";
    	}
    	else return "redirect:/join";
    }
}