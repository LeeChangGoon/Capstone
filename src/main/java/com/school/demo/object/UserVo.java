package com.school.demo.object;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserVo {
	
	private String username;
	private String password;
	private String name;
	private String phoneNumber;
	private String role;
	
}
