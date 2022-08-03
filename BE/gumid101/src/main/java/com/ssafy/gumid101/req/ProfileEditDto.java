package com.ssafy.gumid101.req;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileEditDto {
	private String nickName;
	@Min(value = 0,message = "키 설정은 0 이상이여야 합니다.")
	private Integer height;
	@Min(value = 0,message = "몸무게는 0 이상이여야 합니다.")
	private Integer  weight;
}
