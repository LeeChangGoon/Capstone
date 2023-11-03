package com.school.demo.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargerMarkers {
    private String name;
    private String address;    
	private int slots;
	private int available_slots; 
}
