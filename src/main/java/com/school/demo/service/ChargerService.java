package com.school.demo.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.school.demo.ErrorCode.ResultCode;
import com.school.demo.object.Chargers;


public interface ChargerService {
	
	List<Chargers> getChargers(int page, int size);
	
	List<Chargers> getAllChargers();
	
	List<Chargers> findChargers(String searchaddr);
	
	ResultCode deleteChargers(int no);
	
	Chargers findChargerByname(String chrstn_nm);
	
	int getTotalCount();
	
	List<String> getType(String chrstn_nm);
}
