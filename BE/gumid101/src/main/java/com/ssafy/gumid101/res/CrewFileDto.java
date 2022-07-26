package com.ssafy.gumid101.res;

import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.ImageFileDto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 크루 생성 후 내용을 반환하는 데 사용 
 * @author start
 *
 */
@AllArgsConstructor
@Data
public class CrewFileDto {
	private final CrewDto crewDto;
	private final ImageFileDto imageFileDto;
}
