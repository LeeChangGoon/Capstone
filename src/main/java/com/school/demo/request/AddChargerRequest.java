package com.school.demo.request;

import lombok.Data;

@Data
public class AddChargerRequest {
	
	private String location;
	
	private int total_slots;
}
