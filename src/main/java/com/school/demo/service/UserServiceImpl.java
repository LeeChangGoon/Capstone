package com.school.demo.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.school.demo.ErrorCode.ResultCode;
import com.school.demo.exception.CustomException;
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
	public ResultCode joinUser(String username, String password, String name, String phoneNumber) {
		String regex = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
		//username 중복 거르기
		if(userMapper.findByUsername(username)!=null) {
    		throw new CustomException(ResultCode.CONFLICT.getStatusCode(), "해당 username의 사용자가 이미 존재합니다.");
		}
		//phoneNumber 형식 검사
		else if(!phoneNumber.matches(regex)) {
			throw new CustomException(ResultCode.BAD_REQUEST.getStatusCode(), "전화번호 형식으로 입력해주세요.");
		}
		else {
			String encodedPassword = passwordEncoder.encode(password);
			int result = userMapper.joinUser(username,encodedPassword, name, phoneNumber);
			if(result==1)
				return ResultCode.SUCCESS;
			else throw new CustomException(ResultCode.INTERNAL_SERVER_ERROR.getStatusCode(), "회원가입 실패");
			}
		}
	//관리자인지 체크
	@Override
	public boolean checkAdmin(String username) {
		
		String role = userMapper.checkAdmin(username);
		if(role.equals("admin")) return true;
		else return false;
	}
	//이용자 삭제
	@Override
	public ResultCode deleteUser(String username) {
		
		if(userMapper.deleteUser(username)==1) {
			return ResultCode.SUCCESS;
		}
		else throw new CustomException(ResultCode.NOT_FOUND.getStatusCode(), "해당 이용자를 찾지 못했습니다.");
	}
	//관리자 지정
	@Override
	public ResultCode setAdmin(String username) {
		if(userMapper.setAdmin(username)>0) return ResultCode.SUCCESS;
		else throw new CustomException(ResultCode.INTERNAL_SERVER_ERROR.getStatusCode(), "관리자 지정 실패");
		
	}
	//사용자로 변경
	@Override
	public ResultCode setUser(String username) {
		if(userMapper.setAdmin(username)>0) return ResultCode.SUCCESS;
		else throw new CustomException(ResultCode.INTERNAL_SERVER_ERROR.getStatusCode(), "사용자 지정 실패");
		
	}
}
    
