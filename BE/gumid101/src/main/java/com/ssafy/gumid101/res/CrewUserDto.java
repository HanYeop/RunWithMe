package com.ssafy.gumid101.res;

import java.time.LocalDateTime;

import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CrewUserDto {

	CrewDto crewDto;
	UserDto userDto;
	LocalDateTime crewUserRegTime;
}
