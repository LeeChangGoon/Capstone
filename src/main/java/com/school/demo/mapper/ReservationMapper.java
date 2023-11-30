package com.school.demo.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.school.demo.object.Reservations;

@Mapper
public interface ReservationMapper {
	
	public Reservations findReservationById(@Param("id") int id);
	
	public int makeReservation(@Param("username") String username, @Param("chrgr_no") int chrgr_no, @Param("chrstn_nm") String chrstn_nm, @Param("chrgr_type") String chrgr_type,
								@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

	List<Reservations> findReservationByUsernameAndTime(@Param("username") String username, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
	
	public int deleteReservation(@Param("id") int id);
	
	public int modifyReservation(@Param("id") int id, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

	public Reservations findReservationByTime(@Param("chrgr_no") int chrgr_no, @Param("startTime")LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);
	
	List<Reservations> getReservations();
	
	List<Reservations> getReservationsBychrgr(@Param("chrgr_no") int chrgr_no);
	
	List<Reservations> findReservationByUsername(@Param("username") String username);
}
