package com.ssafy.gumid101.res;

import java.time.LocalDateTime;

import com.ssafy.gumid101.dto.CrewBoardDto;
import com.ssafy.gumid101.dto.ImageFileDto;

import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CrewBoardFileDto {
	
	

	@ApiParam(value = "크루  글 번호")
	private Long crewBoardSeq;
	
	@ApiParam(value = "글 내용")
	private String crewBoardContent;
	
	private LocalDateTime crewBoardRegTime;
	
	
	@ApiParam(value = "글 쓴 유저 SEQ")
	private String userNickName;
	
	@ApiParam(value = "글쓴 유저 SEQ")
	private Long userSeq;
	
	@ApiParam(value = "글쓴 크루")
	private String crewName;
	
	private  ImageFileDto imageFileDto;
}
