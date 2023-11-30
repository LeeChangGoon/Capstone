package com.school.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.school.demo.ErrorCode.ResultCode;
import com.school.demo.object.Members;
import com.school.demo.object.Reservations;
import com.school.demo.object.UserVo;

public interface UserService {
	
	List<Members> getUsers();
	ResultCode joinUser(String username, String password, String name, String phoneNumber);
	ResultCode deleteUser(String username);
	ResultCode setAdmin(String username);
	ResultCode setUser (String username);
	boolean checkAdmin(String username);
}
