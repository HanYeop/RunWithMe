package com.ssafy.gumid101.res;

import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.ImageFileDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CrewFileDto {
	private final CrewDto crewDto;
	private final ImageFileDto imageFileDto;
}
