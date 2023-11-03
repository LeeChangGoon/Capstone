package com.school.demo.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.school.demo.ErrorCode.ResultCode;
import com.school.demo.exception.CustomException;
import com.school.demo.mapper.ChargerMapper;
import com.school.demo.mapper.ReservationMapper;
import com.school.demo.mapper.UserMapper;
import com.school.demo.object.Chargers;
import com.school.demo.object.Reservations;
import com.school.demo.request.ReservationRequest;

@Service
public class ReservationServiceImpl implements ReservationService{
	
	private final ReservationMapper reservationMapper;
	private final ChargerMapper chargerMapper;
	
    @Autowired
    public ReservationServiceImpl(ReservationMapper reservationMapper, ChargerMapper chargerMapper) {
        this.reservationMapper = reservationMapper;
        this.chargerMapper = chargerMapper;
    }
    
    @Override
    public ResultCode makeReservation(String username, String chrstn_nm, LocalDateTime startTime, LocalDateTime endTime) {    	
    	int chrgr_no = chargerMapper.findChargerByname(chrstn_nm).getNo();
    	Chargers charger = chargerMapper.findChargerByname(chrstn_nm);
        
        //자리 없을때 예약 불가
        if (charger.getAvailable_slots() == 0) {
            throw new CustomException(ResultCode.CONFLICT.getStatusCode(), "가용 자리가 없습니다.");
        }
        
        // 동일 사용자가 같은 시간에 두 번 이상 예약 불가
        Reservations existingReservation = reservationMapper.findReservationByUsernameAndTime(username, startTime, endTime);
        if (existingReservation != null) {
            throw new CustomException(ResultCode.CONFLICT.getStatusCode(), "이미 예약을 하셨습니다.");
        }
        
        // 다른 사용자가 같은 시간대에 예약을 시도하는 경우 예외 처리
        existingReservation = reservationMapper.findReservationByTime(chrgr_no, startTime, endTime);
        if (existingReservation != null) {
            throw new CustomException(ResultCode.CONFLICT.getStatusCode(), "해당 시간대에 이미 예약이 있습니다.");
        }
        
        // 현재 시간 이전의 시간으로 예약 불가
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new CustomException(ResultCode.CONFLICT.getStatusCode(), "이용 가능한 시간이 아닙니다.");
        }
        
        //시작시간 종료시간 관계
        if (!startTime.isBefore(endTime)) {
            throw new CustomException(ResultCode.CONFLICT.getStatusCode(), "시작시간이 종료시간보다 늦습니다.");
        }
        
        
		int result = reservationMapper.makeReservation(username, chrgr_no, chrstn_nm, startTime, endTime);
		
		//예약 생성
	    if (result > 0) {
	        chargerMapper.reservationCharger(chrstn_nm);
	        return ResultCode.SUCCESS;
	    } else {
	        throw new CustomException(ResultCode.INTERNAL_SERVER_ERROR.getStatusCode(), "예약에 실패했습니다.");
	    }
    	
    }

	@Override
	public ResultCode deleteReservation(int id) {
		int chrgr_no = reservationMapper.findReservationById(id).getChrgr_no();
		if(reservationMapper.deleteReservation(id)==1) {
			chargerMapper.plusSlot(chrgr_no);
			return ResultCode.SUCCESS;
		}
		else if(reservationMapper.deleteReservation(id)==0) throw new CustomException(ResultCode.NOT_FOUND.getStatusCode(), "해당 예약이 존재하지 않습니다.");
		else return ResultCode.INTERNAL_SERVER_ERROR;
	}
	
}
