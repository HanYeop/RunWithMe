package com.ssafy.gumid101.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.CreatedDate;

import com.ssafy.gumid101.entity.CrewBoardEntity;
import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.ImageFileEntity;
import com.ssafy.gumid101.entity.UserEntity;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
		if(crewBoard == null)
			return null;
		
		return new CrewBoardDtoBuilder().crewBoardSeq(crewBoard.getCrewBoardSeq()).crewBoardContent(crewBoard.getCrewBoardContent())
//				.boardImageSeq(crewBoard.getImgFile().getImgSeq())
				.build();
	}
	

}