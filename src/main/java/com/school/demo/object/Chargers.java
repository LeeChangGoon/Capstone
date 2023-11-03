package com.school.demo.object;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Chargers {
	
	private int no; //번호
	private String chrstn_nm; //충전소 이름
	private String addr; //주소
	private String dtl_pstn; //상세위치
	private String chrgr_type; //충전기 타입
	private String rpd_elctc_qy; //급속충전량
	private String rmrk; //비고
	private int slots;
	private int available_slots; 
}
