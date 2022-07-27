package com.ssafy.gumid101.dto;

import java.io.Serializable;

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
public class RankingParamsDto implements Serializable {
	@ApiParam(value = "유저 번호")
	private Long userSeq;

	@ApiParam(value = "크루 번호")
	private Long crewSeq;

	@ApiParam(value = "얻어올 크기")
	private Integer size;

	@ApiParam(value = "얻어올 위치")
	private Integer offset;

	@ApiParam(value = "목표 (distance, time)")
	private String type;
}