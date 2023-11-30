package com.school.demo.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationSetRequest {
	private int id;
	private String action;
}