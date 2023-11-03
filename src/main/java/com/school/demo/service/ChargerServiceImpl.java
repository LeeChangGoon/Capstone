package com.school.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.school.demo.mapper.ChargerMapper;
import com.school.demo.object.Chargers;

import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;

@Service
public class ChargerServiceImpl implements ChargerService{
	
	private final ChargerMapper chargerMapper;

    @Autowired
    public ChargerServiceImpl(ChargerMapper chargerMapper) {
        this.chargerMapper = chargerMapper;
    }
    
    @Override
    public List<Chargers> getChargers(int page, int size) {
    	int offset = (page - 1) * size;
        return chargerMapper.getChargers(offset, size);
    }
    
    @Override
    public List<Chargers> findChargers(String searchaddr){
    	return chargerMapper.findChargers(searchaddr);
    }

    @Override
    public List<Chargers> getAllChargers(){
    	return chargerMapper.getAllChargers();
    }
	@Override
	public int getTotalCount() {
		return chargerMapper.getTotalCount();
	}

}
