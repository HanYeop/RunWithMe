package com.ssafy.gumid101.dto;

import java.io.Serializable;

import com.ssafy.gumid101.entity.CrewBoardEntity;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
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

	@ApiParam(value = "크루 내 글 번호")
	private Long crewBoardSeq;

	@ApiParam(value = "글 내용")
	private String crewBoardContent;
	
//	@ApiModelProperty(hidden = true)
//	private Long boardImageSeq;
	
	public static CrewBoardDto of(CrewBoardEntity crewBoard) {
		return new CrewBoardDtoBuilder().crewBoardSeq(crewBoard.getCrewBoardSeq()).crewBoardContent(crewBoard.getCrewBoardContent())
//				.boardImageSeq(crewBoard.getImgFile().getImgSeq())
				.build();
	}
	

}