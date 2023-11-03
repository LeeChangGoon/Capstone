package com.school.demo.object;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Reservations {
	
	private int id;
	private String username;
	private int chrgr_no;
	private String chrstn_nm;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	

}
