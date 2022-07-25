package com.ssafy.gumid101.res;

import com.ssafy.gumid101.dto.CrewBoardDto;
import com.ssafy.gumid101.dto.ImageFileDto;

import lombok.Data;

@Data
public class CrewBoardFileDto {
	private final CrewBoardDto crewBoardDto;
//	private final Long imageSeq;
	private final ImageFileDto imageDto;
}
