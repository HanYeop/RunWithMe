package com.ssafy.gumid101.dto;

import java.io.Serializable;

import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.user.Role;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 레코드 리스트
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordParamsDto implements Serializable {
	@ApiParam(value = "유저 번호")
	private Long userSeq;

	@ApiParam(value = "크루 번호")
	private Long crewSeq;

	@ApiParam(value = "얻어올 개수")
	private Integer size;

	@ApiParam(value = "맥스 뭐시기")
	private Integer maxRunRecordSeq;
	
	@ApiParam(value = "년")
	private Integer year;
	
	@ApiParam(value = "월")
	private Integer month;
}