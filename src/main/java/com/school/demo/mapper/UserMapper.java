package com.school.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.school.demo.object.Members;
import com.school.demo.object.Reservations;
import com.school.demo.object.UserVo;

@Mapper
public interface UserMapper {

	List<Members> getUsers();
	
	List<Reservations> getReservations();
	
	UserVo findByUsername(String username);
	
	int joinUser(@Param("username") String username, @Param("password") String password, @Param("name") String name,
			    @Param("phoneNumber") String phoneNumber);
	
	String checkAdmin(String username);
	
	int updateRole(String username);
	
	int setAdmin(String username);
	
	int setUser(String username);
	
	int deleteUser(String username);

}
