package com.school.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.school.demo.ErrorCode.ResultCode;
import com.school.demo.object.Reservations;

public interface ReservationService {
	
	ResultCode makeReservation(String username, String chrstn_nm, String chrgr_type, LocalDateTime startTime, LocalDateTime endTime);
	
	ResultCode deleteReservation(int id);
	
	ResultCode deleteReservation_User(String username, int id);
	
	ResultCode modifyReservation(String username, int id, LocalDateTime startTime, LocalDateTime endTime);
	
	void setAddr(List<Reservations> rv);
	
	void checkNow(LocalDateTime now);

	List<Reservations> getReservations();

	List<Reservations> getReservationByname(String username);
}
