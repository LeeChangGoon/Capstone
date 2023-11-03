package com.school.demo.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.school.demo.mapper.UserMapper;
import com.school.demo.object.Chargers;
import com.school.demo.object.Members;
import com.school.demo.object.Reservations;
import com.school.demo.object.UserVo;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserMapper userMapper;
	private final BCryptPasswordEncoder passwordEncoder;
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
		this.passwordEncoder = new BCryptPasswordEncoder();
    }

	@Override
	public List<Members> getUsers() {
		List<Members> members = userMapper.getUsers();
		return members;
	}
	
	@Override
	public List<Reservations> getReservations(){
		List<Reservations> reservations = userMapper.getReservations();
		return reservations;
	}
	@Override
	public int joinUser(String username, String password, String name, String phoneNumber) {
		String encodedPassword = passwordEncoder.encode(password);
		int result = userMapper.joinUser(username,encodedPassword, name, phoneNumber);
		if(result>0)
			return result;
		else return 0;
		}
	@Override
	public boolean checkAdmin(String username) {
		
		String role = userMapper.checkAdmin(username);
		if(role.equals("admin")) return true;
		else return false;
	}
	
	@Override
	public int deleteUser(String username) {
		
		int result = userMapper.deleteUser(username);
		
		if(result>0) return result;
		else return 0;
	}
	
	@Override
	public int setAdmin(String username) {
		int result = userMapper.setAdmin(username);
		
		if(result>0) return result;
		else return 0;
	}
	
	@Override
	public int setUser(String username) {
		int result = userMapper.setUser(username);
		
		if(result>0) return result;
		else return 0;
	}
}
    
