package com.school.demo.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.school.demo.request.ReservationRequest;
import com.school.demo.service.ChargerService;
import com.school.demo.service.ReservationService;
import com.school.demo.service.UserService;


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
	
    @GetMapping("/home")
    public String HomePage(Model model) {
    	
        List<Chargers> chargers = chargerService.getChargers(1,40);
        model.addAttribute("chargers", chargers);
        
        List<ChargerMarkers> chargermarkers = chargers.stream()
                .map(charger -> new ChargerMarkers(charger.getChrstn_nm(), charger.getAddr(), charger.getSlots(), charger.getAvailable_slots())) //charger = 스트림에 있는 각 chargers객체
                .collect(Collectors.toList());

            model.addAttribute("chargermarkers", chargermarkers);

        
        return "home";
    }
    
    @GetMapping("/reservation")
    public String ReservationPage(@RequestParam String name, Model model) {
        model.addAttribute("name", name);
        return "reservation";
    }
    
    //충전소 주소 검색
    @GetMapping("/findChargers")
    public String findChargers(String searchaddr, RedirectAttributes redirectAttributes) {
        List<Chargers> results = chargerService.findChargers(searchaddr);
        redirectAttributes.addFlashAttribute("searchresults", results);
        
        return "redirect:/user/home";
    }

    
    @PostMapping("/makeReservation")
    public String makeReservation(@ModelAttribute ReservationRequest requestBody) {
        String username;
        String chrstn_nm = requestBody.getChrstn_nm();
        LocalDateTime startTime = LocalDateTime.parse(requestBody.getStartTime(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.getEndTime(), DateTimeFormatter.ISO_DATE_TIME);
        
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            throw new IllegalStateException("인증사용자가 아닙니다.");
        }
        
        ResultCode result = reservationService.makeReservation(username, chrstn_nm, startTime, endTime);
        
        if (result == ResultCode.SUCCESS) {
            return "redirect:/user/home";
        } else {
            throw new CustomException(result.getStatusCode(), "Reservation failed");
        }
    }
}
