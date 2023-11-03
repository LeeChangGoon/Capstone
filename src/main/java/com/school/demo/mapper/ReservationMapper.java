package com.school.demo.mapper;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.school.demo.object.Reservations;

@Mapper
public interface ReservationMapper {
	
	public Reservations findReservationById(@Param("id") int id);
	
	public int makeReservation(@Param("username") String username, @Param("chrgr_no") int chrgr_no, @Param("chrstn_nm") String chrstn_nm, 
								@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

	public Reservations findReservationByUsernameAndTime(@Param("username") String username, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
	
	public int deleteReservation(@Param("id") int id);

	public Reservations findReservationByTime(@Param("chrgr_no") int chrgr_no, @Param("startTime")LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);
}
