package com.ssafy.gumid101.req;

import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileEditDto {
	@Pattern(regexp = "[a-zA-Z1-9가-힣ㄱ-ㅎ]{4,20}",message = "닉네임(4~20) 특수문자 불가")
	private String nickName;
	private Integer height;
	private Integer  weight;
}
