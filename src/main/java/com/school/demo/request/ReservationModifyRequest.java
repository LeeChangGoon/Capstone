package com.school.demo.request;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationModifyRequest {
	private int id;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
}
