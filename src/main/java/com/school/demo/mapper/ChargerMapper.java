package com.school.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.school.demo.object.Chargers;


@Mapper
public interface ChargerMapper {
	
	List<Chargers> getChargers(@Param("offset") int offset, @Param("limit") int limit);
	
	List<Chargers> getAllChargers();
	
	List<Chargers> findChargers(@Param("searchaddr") String searchaddr); //주소로 충전소 검색
	
	Chargers findChargerByname(@Param("chrstn_nm") String chrstn_nm);
	
	void reservationCharger(@Param("name") String name); //예약하면 하나 자리 하나 감소
	
	int addChargers(@Param("location") String location, @Param("total_slots") int total_slots);

	int getTotalCount();
	
	void insertChargers(Chargers chData);
	
	void plusSlot(@Param("chrgr_no") int chrgr_no);
}
