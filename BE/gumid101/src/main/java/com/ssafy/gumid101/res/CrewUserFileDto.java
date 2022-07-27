package com.ssafy.gumid101.res;

import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.UserDto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CrewUserFileDto {
	CrewDto crewDto;
	UserDto userDto;
	ImageFileDto ImageFileDto;
}
