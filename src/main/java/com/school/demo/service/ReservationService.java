package com.school.demo.service;

import java.time.LocalDateTime;

import com.school.demo.ErrorCode.ResultCode;

public interface ReservationService {
	
	ResultCode makeReservation(String username, String chrstn_nm, LocalDateTime startTime, LocalDateTime endTime);
	
	ResultCode deleteReservation(int id);
}
