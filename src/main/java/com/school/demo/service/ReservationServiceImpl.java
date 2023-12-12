package com.school.demo.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.school.demo.ErrorCode.ResultCode;
import com.school.demo.exception.CustomException;
import com.school.demo.mapper.ChargerMapper;
import com.school.demo.mapper.ReservationMapper;
import com.school.demo.object.Chargers;
import com.school.demo.object.Reservations;

@Service
public class ReservationServiceImpl implements ReservationService{
	
	private final ReservationMapper reservationMapper;
	private final ChargerMapper chargerMapper;
	
    @Autowired
    public ReservationServiceImpl(ReservationMapper reservationMapper, ChargerMapper chargerMapper) {
        this.reservationMapper = reservationMapper;
        this.chargerMapper = chargerMapper;
    }
    
    //예약 생성
    @Override
    public ResultCode makeReservation(String username, String chrstn_nm, String chrgr_type, LocalDateTime startTime, LocalDateTime endTime) {
        int chrgr_no = chargerMapper.findChargerBynameAndType(chrstn_nm, chrgr_type).getNo();
        synchronized(this){
        	// 동일 사용자가 예약했던 시간대에 두 번 이상 예약 불가
        	List<Reservations> existingReservation = reservationMapper.findReservationByUsernameAndTime(username, startTime, endTime);
        	if (!existingReservation.isEmpty()) {
        		throw new CustomException(ResultCode.CONFLICT.getStatusCode(), "이미 예약을 하셨습니다.");
        	}

        	// 다른 사용자가 예약을 한 시간대에 예약을 시도하는 경우 예외 처리
        	Reservations existingReservation2 = reservationMapper.findReservationByTime(chrgr_no, startTime, endTime);
        	if (existingReservation2 != null) {
        		throw new CustomException(ResultCode.CONFLICT.getStatusCode(), "해당 시간대에 이미 예약이 있습니다.");
        	}

        	// 현재 시간 이전의 시간으로 예약 불가
        	else if (startTime.isBefore(LocalDateTime.now())) {
        		throw new CustomException(ResultCode.CONFLICT.getStatusCode(), "이용 가능한 시간이 아닙니다.");
        	}

        	// 시작시간 종료시간 관계
        	else if (!startTime.isBefore(endTime)) {
        		throw new CustomException(ResultCode.CONFLICT.getStatusCode(), "시작시간이 종료시간보다 늦습니다.");
        	}

        	// 모든 예외 케이스 통과 후 예약 생성
        	if (reservationMapper.makeReservation(username, chrgr_no, chrstn_nm, chrgr_type, startTime, endTime)>0) {
        		return ResultCode.SUCCESS;
        	} else {
        		throw new CustomException(ResultCode.INTERNAL_SERVER_ERROR.getStatusCode(), "예약에 실패했습니다.");
        	}
        }
    }

    //예약 삭제(관리자용)
	@Override
	public ResultCode deleteReservation(int id) {
		Reservations reservation = reservationMapper.findReservationById(id);
		if(reservation==null) throw new CustomException(ResultCode.NOT_FOUND.getStatusCode(), "해당 예약이 존재하지 않습니다.");

		int chrgr_no = reservation.getChrgr_no();
		synchronized(this) {
			
			if(reservationMapper.deleteReservation(id)>=1) {
			chargerMapper.plusSlot(chrgr_no);
			return ResultCode.SUCCESS;
		}
			else if(reservationMapper.deleteReservation(id)==0) throw new CustomException(ResultCode.NOT_FOUND.getStatusCode(), "해당 예약이 존재하지 않습니다.");
			else return ResultCode.INTERNAL_SERVER_ERROR;
		}
	}
	
    //예약 삭제(사용자용)
	@Override
	public ResultCode deleteReservation_User(String username, int id) {
		Reservations reservation = reservationMapper.findReservationById(id);
		if(reservation==null) throw new CustomException(ResultCode.NOT_FOUND.getStatusCode(), "해당 예약이 존재하지 않습니다.");
		int chrgr_no = reservation.getChrgr_no();
		synchronized(this) {
			if(!reservation.getUsername().equals(username)) throw new CustomException(ResultCode.FORBIDDEN.getStatusCode(), "해당 사용자의 예약 정보가 아닙니다.");
			if(reservationMapper.deleteReservation(id)>=1) {
			chargerMapper.plusSlot(chrgr_no);
			return ResultCode.SUCCESS;
		}
			else if(reservationMapper.deleteReservation(id)==0) throw new CustomException(ResultCode.NOT_FOUND.getStatusCode(), "해당 예약이 존재하지 않습니다.");
			else return ResultCode.INTERNAL_SERVER_ERROR;
		}
	}
	
	//예약 수정
	@Override
	public ResultCode modifyReservation(String username, int id, LocalDateTime startTime, LocalDateTime endTime) {
		Reservations reservation=reservationMapper.findReservationById(id);
		int chrgr_no=reservation.getChrgr_no();
		
        synchronized(this){
        	
        	if(!reservation.getUsername().equals(username)) throw new CustomException(ResultCode.FORBIDDEN.getStatusCode(), "해당 사용자의 예약 정보가 아닙니다.");
   
        	// 다른 사용자가 예약을 한 시간대에 예약을 시도하는 경우 예외 처리
        	Reservations existingReservation2 = reservationMapper.findReservationByTime(chrgr_no, startTime, endTime);
        	if (existingReservation2 != null) {
        		throw new CustomException(ResultCode.CONFLICT.getStatusCode(), "해당 시간대에 이미 예약이 있습니다.");
        	}

        	// 현재 시간 이전의 시간으로 예약 불가
        	else if (startTime.isBefore(LocalDateTime.now())) {
        		throw new CustomException(ResultCode.CONFLICT.getStatusCode(), "이용 가능한 시간이 아닙니다.");
        	}

        	// 시작시간 종료시간 관계
        	else if (!startTime.isBefore(endTime)) {
        		throw new CustomException(ResultCode.CONFLICT.getStatusCode(), "시작시간이 종료시간보다 늦습니다.");
        	}

        	int result = reservationMapper.modifyReservation(id,startTime, endTime);

        	// 모든 예외 케이스 통과 후 예약 생성
        	if (result > 0) {
        		return ResultCode.SUCCESS;
        	} else {
        		throw new CustomException(ResultCode.INTERNAL_SERVER_ERROR.getStatusCode(), "예약에 실패했습니다.");
        	}
        }
	}
	//현재시간 체크해서 예약리스트 관리
	@Override
	public void checkNow(LocalDateTime now) {
	    List<Reservations> reservationList = reservationMapper.getReservations();
	    Map<Integer, Integer> ReservationCount = new HashMap<>(); // 충전기별 이용 중인 예약 개수를 저장하는 Map
	    for (Reservations x : reservationList) {
	    	if(x.getStartTime().isBefore(now) && x.getEndTime().isAfter(now)) {
	    		int chrgr_no = x.getChrgr_no();
	            ReservationCount.put(chrgr_no, ReservationCount.getOrDefault(chrgr_no, 0)+1);
	    		}
	    	else if(x.getEndTime().isBefore(now)) deleteReservation(x.getId()); //현재 시간이 지난 예약 삭제
	    	}
	    List<Chargers> chargerList = chargerMapper.getAllChargers();
	    
	    //현재 예약이 있는 충전소의 자리 조정
	    for (Map.Entry<Integer, Integer> entry : ReservationCount.entrySet()) {
	        int chrgr_no = entry.getKey();
	        int reservationCount = entry.getValue();
	        
	        chargerMapper.slotMinus(chrgr_no, reservationCount);
	        }
	    
	    // 예약이 없는 충전소의 자리를 초기화
	    for (Chargers charger : chargerList) {
	        int chrgr_no = charger.getNo();
	        if (!ReservationCount.containsKey(chrgr_no)) {
	        	chargerMapper.setSlot(chrgr_no);
	            }
	        }
	    }


	@Override
	public List<Reservations> getReservations(){
		List<Reservations> reservations = reservationMapper.getReservations();
		return reservations;
	}
	@Override
	public List<Reservations> getReservationByname(String username){
		List<Reservations> reservations = reservationMapper.findReservationByUsername(username);
		return reservations;

	}

	@Override
	public void setAddr(List<Reservations> rv) {
	    for(Reservations x : rv) {
	    	x.setAddr(chargerMapper.findChargerBynameAndType(x.getChrstn_nm(), x.getChrgr_type()).getAddr());
	    }
		
	}
	
}
