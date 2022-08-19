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
public class RecruitmentParamsDto implements Serializable {
	
	@ApiParam(value = "오프셋으로 부터 보여줄 개수")
	private Long size; 
	
	//2022-07-26 페이징 긴급 수정
	@ApiParam(value = "몇 번째부터 얻어올지,offset => maxCrueSeq")
	private Long maxCrewSeq;
	
	@ApiParam(value = "크루 이름")
	private String title;
	@ApiParam(value = "크루 시작시점(yyyy-MM-dd HH:mm:ss)")
	private String startDay;
	@ApiParam(value = "크루 종료시점(yyyy-MM-dd HH:mm:ss)")
	private String endDay;
	@ApiParam(value = "활동 시작 시간(HH:mm:ss)")
	private String startTime;
	@ApiParam(value = "활동 종료 시간(HH:mm:ss)")
	private String endTime; //끝나는 시간
	@ApiParam(value = "포인트 최소치")
	private Integer pointMin;
	@ApiParam(value = "포인트 최대치")
	private Integer pointMax;
	@ApiParam(value = "목표 타입(distance, time)")
	private String purposeType;
	@ApiParam(value = "목표 최소치(거리 : m, 시간 : 초)")
	private Integer purposeMinValue;
	@ApiParam(value = "목표 최대치(거리 : m, 시간 : 초)")
	private Integer purposeMaxValue;
	@ApiParam(value = "1주일 내 목표일 수 최소치")
	private Integer goalMinDay;
	@ApiParam(value = "1주일 내 목표일 수 최대치")
	private Integer goalMaxDay; //1주일에 행해야하는 횟수 최대
	
	
	private CrewSortType sortType;
	
}