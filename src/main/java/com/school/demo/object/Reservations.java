package com.school.demo.object;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Reservations {
	
	private int id;
	private String username;
	private int chrgr_no;
	private String chrstn_nm;
	private String addr;
	private String chrgr_type;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	

}
