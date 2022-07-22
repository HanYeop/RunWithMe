package com.ssafy.gumid101.req;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileEditDto {

	private String nickName;
	private Integer height;
	private Integer  weight;
}
