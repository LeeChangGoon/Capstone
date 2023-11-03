package com.school.demo.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.school.demo.object.Chargers;


public interface ChargerService {
	
	List<Chargers> getChargers(int page, int size);
	
	List<Chargers> getAllChargers();
	
	List<Chargers> findChargers(String searchaddr);

	int getTotalCount();

}
