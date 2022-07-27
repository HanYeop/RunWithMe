package com.ssafy.gumid101.res;

import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Data
public class UserFileDto {

	private  UserDto user;
	private  ImageFileDto imgFileDto;
	
	
}
