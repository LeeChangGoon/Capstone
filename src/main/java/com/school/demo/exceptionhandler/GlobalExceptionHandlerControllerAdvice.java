package com.school.demo.exceptionhandler;

import com.school.demo.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandlerControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException ex, Model model) {
        log.error("CustomException 발생: {}", ex.getMessage());
        
        // 모델에 에러 메시지 추가
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", ex.getStatusCode());
        // 에러 페이지로 포워드
        return "errorPage";
    }
    
    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException ex, Model model) {
    	log.error("로그인 오류 발생: {}", ex.getMessage());
    	
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", HttpStatus.NOT_FOUND);
		return "errorPage";
    	
    }
    
    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException ex, Model model) {
    	log.error("로그인 오류 발생: {}", ex.getMessage());
    	
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", HttpStatus.FORBIDDEN);
		return "errorPage";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        log.error("예외 발생: ", ex);
        
        // 모델에 에러 메시지 추가
        model.addAttribute("errorMessage", "서버에서 오류가 발생했습니다.");
        model.addAttribute("errorCode", 500);
        // 에러 페이지로 포워드
        return "errorPage";
    }
}


