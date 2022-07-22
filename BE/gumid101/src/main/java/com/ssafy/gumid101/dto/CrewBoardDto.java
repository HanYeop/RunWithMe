package com.ssafy.gumid101.dto;

import java.io.Serializable;

import com.ssafy.gumid101.entity.CrewBoardEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 직렬화 기능을 가진 User클래스
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrewBoardDto implements Serializable {

	private Long crewBoardSeq;

	private String crewBoardContent;
	
	public static CrewBoardDto of(CrewBoardEntity crewBoard) {
		return new CrewBoardDtoBuilder().crewBoardSeq(crewBoard.getCrewBoardSeq()).crewBoardContent(crewBoard.getCrewBoardContent()).build();
	}
	

}