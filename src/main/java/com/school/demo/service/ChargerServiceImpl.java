package com.school.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.school.demo.ErrorCode.ResultCode;
import com.school.demo.exception.CustomException;
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
    public Chargers findChargerByname(String chrstn_nm){
    	return chargerMapper.findChargerByname(chrstn_nm);
    }
    
    @Override
    public List<Chargers> getAllChargers(){
    	return chargerMapper.getAllChargers();
    }
	@Override
	public int getTotalCount() {
		return chargerMapper.getTotalCount();
	}

	@Override
	public List<String> getType(String chrstn_nm) {
		return chargerMapper.findTypes(chrstn_nm);
	}

	@Override
	public ResultCode deleteChargers(int no) {
		
		if(chargerMapper.deleteCharger(no)==1) return ResultCode.SUCCESS;
		else if(chargerMapper.deleteCharger(no)==0) throw new CustomException(ResultCode.NOT_FOUND.getStatusCode(),"해당 번호의 충전소를 찾지 못했습니다");
		else throw new CustomException(ResultCode.INTERNAL_SERVER_ERROR.getStatusCode(), "관리자 요청 바람");
	}

}
