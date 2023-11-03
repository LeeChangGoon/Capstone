package com.school.demo.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSetRequest {
	private String username;
	private String action;

}
