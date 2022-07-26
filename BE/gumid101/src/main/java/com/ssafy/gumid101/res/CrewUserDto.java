package com.ssafy.gumid101.res;

import java.time.LocalDateTime;

import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 크루 가입 후 반환
 * @author start
 *
 */
@AllArgsConstructor
@Data
public class CrewUserDto {

	CrewDto crewDto;
	UserDto userDto;
	LocalDateTime crewUserRegTime;
}
