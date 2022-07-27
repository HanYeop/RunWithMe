package com.ssafy.gumid101.dto;

import java.io.Serializable;

import com.ssafy.gumid101.entity.CrewTotalRecordEntity;

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
public class CrewTotalRecordDto implements Serializable {

	@ApiParam(value = "전체기록번호")
	private Long totalRecordSeq;

	@ApiParam(value = "전체 달린 시간 (초)")
	private Integer totalTime;

	@ApiParam(value = "전체 달린 거리 (m)")
	private Integer totalDistance;

	@ApiParam(value = "가장 많이 달린 시간 (초)")
	private Integer totalLongestTime;
	
	@ApiParam(value = "가장 많이 달린 거리 (m)")
	private Integer totalLongestDistance;
	
	@ApiParam(value = "전체 평균 속도 (km/h or m/s 추후 선택해야함)")
	private Double totalAvgSpeed;
	
	@ApiParam(value = "전체 소모 칼로리 (Kcal)")
	private Double totalCalorie;
	
	public static CrewTotalRecordDto of(CrewTotalRecordEntity crewTotalRecord) {
		if(crewTotalRecord == null)
			return null;
		return new CrewTotalRecordDtoBuilder()
				.totalRecordSeq(crewTotalRecord.getTotalRecoredSeq())
				.totalCalorie(crewTotalRecord.getTotalCalorie())
				.totalDistance(crewTotalRecord.getTotalDistance())
				.totalTime(crewTotalRecord.getTotalTime())
				.totalLongestTime(crewTotalRecord.getTotalLongestTime())
				.totalLongestDistance(crewTotalRecord.getTotalLongestDistance())
				.build();
	}
}