package com.ssafy.gumid101.res;

import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.ImageFileDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 크루 생성 후 내용을 반환하는 데 사용 
 * @author start
 *
 */
@AllArgsConstructor
@Builder
@Data
public class EndCrewFileDto {
	@Data
	@AllArgsConstructor
	@Builder
	public static class ProcessInfo{
		Integer totalGoals;
		Integer myGoals;
	}
	private final CrewDto crewDto;
	private final ProcessInfo processInfo;
	private final ImageFileDto imageFileDto;
}
