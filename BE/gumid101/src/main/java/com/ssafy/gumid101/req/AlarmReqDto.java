package com.ssafy.gumid101.req;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AlarmReqDto {

	@NotNull(message = "알람을 보내는 유저를 지정해야합니다.")
	private Long userSeq;
	
	
	@NotNull(message="제목을 지정하셔야 합니다.")
	private String title;
	
	private String body;
}
