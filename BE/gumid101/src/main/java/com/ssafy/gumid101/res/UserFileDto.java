package com.ssafy.gumid101.res;

import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.UserDto;

import lombok.Data;
import lombok.Getter;

@Data
public class UserFileDto {

	private final UserDto user;
	private final ImageFileDto imgFileDto;
	
	
}
