package com.school.demo.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.school.demo.ErrorCode.ResultCode;
import com.school.demo.exception.CustomException;
import com.school.demo.mapper.ChargerMapper;
import com.school.demo.object.ChargerMarkers;
import com.school.demo.object.Chargers;
import com.school.demo.object.Reservations;
import com.school.demo.request.ReservationModifyRequest;
import com.school.demo.request.ReservationRequest;
import com.school.demo.request.ReservationSetRequest;
import com.school.demo.service.ChargerService;
import com.school.demo.service.ReservationService;
import com.school.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/user")
public class UserController {

	private UserService userService;
	private ChargerService chargerService;
	private ReservationService reservationService;
	
	@Autowired
	public UserController (UserService userService, ChargerService chargerService, ReservationService reservationService) {
		this.userService = userService;
		this.chargerService = chargerService;
		this.reservationService = reservationService;
		}
	
	//메인페이지
    @GetMapping("/home")
    public String HomePage(Model model) {
    	
    	reservationService.checkNow(LocalDateTime.now());
    	
        List<Chargers> chargers = chargerService.getChargers(1,40);
        model.addAttribute("chargers", chargers);
        
        List<ChargerMarkers> chargermarkers = chargers.stream()
                .map(charger -> new ChargerMarkers(charger.getChrstn_nm(), charger.getAddr(), charger.getSlots(), charger.getAvailable_slots())) //charger = 스트림에 있는 각 chargers객체
                .collect(Collectors.toList());

            model.addAttribute("chargermarkers", chargermarkers);

        
        return "home";
    }
    
    //사용자 페이지
    @GetMapping("/main")
    public String userMainPage(Model model) {
    	reservationService.checkNow(LocalDateTime.now());

    	String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            throw new IllegalStateException("인증사용자가 아닙니다.");
        }
        
	    List<Reservations> reservations = reservationService.getReservationByname(username);
	    reservationService.setAddr(reservations);
	    model.addAttribute("reservations", reservations);
    	return "user";
    }
    
    //예약 페이지
    @GetMapping("/reservation")
    public String ReservationPage(@RequestParam String name, Model model) {
    	
    	reservationService.checkNow(LocalDateTime.now());
        model.addAttribute("name", name);
        List<String> Types = chargerService.getType(name);
        model.addAttribute("types", Types);
        return "reservation";
    }
    
    //충전소 주소 검색
    @GetMapping("/findChargers")
    public String findChargers(String searchaddr, RedirectAttributes redirectAttributes) {
        List<Chargers> results = chargerService.findChargers(searchaddr);
        redirectAttributes.addFlashAttribute("searchresults", results);
        
        return "redirect:/user/home";
    }

    //예약생성
    @PostMapping("/makeReservation")
    public String makeReservation(@ModelAttribute ReservationRequest requestBody) {
        String username;
        String chrstn_nm = requestBody.getChrstn_nm();
        String chrgr_type = requestBody.getChrgr_type();
        LocalDateTime startTime = LocalDateTime.parse(requestBody.getStartTime(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.getEndTime(), DateTimeFormatter.ISO_DATE_TIME);
        
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            throw new IllegalStateException("인증사용자가 아닙니다.");
        }
        
    	reservationService.checkNow(LocalDateTime.now());

        ResultCode result = reservationService.makeReservation(username, chrstn_nm, chrgr_type, startTime, endTime);
        
        if (result == ResultCode.SUCCESS) {
            return "redirect:/user/home";
        } else {
            throw new CustomException(result.getStatusCode(), "Reservation failed");
        }
    }
    
    //예약 관리
    @PostMapping("/setReservation")
    public String setReservation(@ModelAttribute ReservationSetRequest request, HttpServletRequest servletrequest) {
    	int id=request.getId();
    	String action=request.getAction();
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String username = ((UserDetails) principal).getUsername();
    	
    	if(action.equals("Delete")) {
        	if(reservationService.deleteReservation_User(username,id) == ResultCode.SUCCESS ) {
        		String referer = servletrequest.getHeader("Referer");
        		return "redirect:" + referer;
        	}
        	else throw new CustomException(ResultCode.INTERNAL_SERVER_ERROR.getStatusCode(), "삭제 실패");
    	}
    	else if(action.equals("Modify")) {
    		return "Modify";
    	}
    	return "redirect:/user/main";
    }
    
    //예약시간 수정
    @PostMapping("/modifyReservation")
    public String modifyReservation(@ModelAttribute ReservationModifyRequest request) {
    	int id=request.getId();
    	LocalDateTime startTime=request.getStartTime();
    	LocalDateTime endTime=request.getEndTime();
    	reservationService.checkNow(LocalDateTime.now());
    	
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String username = ((UserDetails) principal).getUsername();
    	if(reservationService.modifyReservation(username,id,startTime,endTime)==ResultCode.SUCCESS) {
    		return "redirect:/user/main";
    	}
    	else return "redirect:/user/main";
    }
}
