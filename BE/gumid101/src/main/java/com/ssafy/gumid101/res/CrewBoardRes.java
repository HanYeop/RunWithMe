package com.ssafy.gumid101.res;

import java.time.LocalDateTime;

import com.ssafy.gumid101.entity.CrewBoardEntity;

import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CrewBoardRes {

	@ApiParam(value = "크루  글 번호")
	private Long crewBoardSeq;
	
	@ApiParam(value = "글 내용")
	private String crewBoardContent;
	
	private LocalDateTime crewBoardRegTime;
	
	@ApiParam(value = "글 쓴 유저 SEQ")
	private String userNickName;
	
	@ApiParam(value = "글쓴 유저 SEQ")
	private Long userSeq;
	
	@ApiParam(value="글쓴 유저 이미지")
	private Long userImgSeq;
	
	@ApiParam(value = "글쓴 크루")
	private String crewName;
	
	public static CrewBoardRes of(CrewBoardEntity entity) {
		
		return CrewBoardRes.builder().crewBoardSeq(entity.getCrewBoardSeq())
		.crewBoardRegTime(entity.getCrewBoardRegTime())
		.crewBoardContent(entity.getCrewBoardContent())
		.crewName(entity.getCrewEntity().getCrewName())
		.userSeq(entity.getUserEntity().getUserSeq())
		.userNickName(entity.getUserEntity().getNickName()).userImgSeq(
				entity.getImgFile() == null ? 0L : entity.getImgFile().getImgSeq()).
		build();

		
	}
}
