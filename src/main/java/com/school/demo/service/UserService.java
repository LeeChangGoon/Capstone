package com.school.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.school.demo.object.Members;
import com.school.demo.object.Reservations;
import com.school.demo.object.UserVo;

public interface UserService {
	List<Members> getUsers();

	List<Reservations> getReservations();
	int joinUser(String username, String password, String name, String phoneNumber);
	int deleteUser(String username);
	int setAdmin(String username);
	int setUser (String username);
	boolean checkAdmin(String username);
}
