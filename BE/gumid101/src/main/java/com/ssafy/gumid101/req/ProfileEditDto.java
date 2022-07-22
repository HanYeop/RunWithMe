package com.ssafy.gumid101.req;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProfileEditDto {

	private String nickName;
	private String height;
	private String weight;
	MultipartFile imageFile;
}
