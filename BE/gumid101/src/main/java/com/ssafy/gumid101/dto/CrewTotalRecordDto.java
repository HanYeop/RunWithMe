package com.ssafy.gumid101.dto;

import java.io.Serializable;

import com.ssafy.gumid101.entity.CrewTotalRecordEntity;

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

	private Long totalRecordSeq;

	private Integer totalTime;

	private Integer totalDistance;

	private Integer totalLongestTime;
	
	private Integer totalLongestDistance;
	
	private Double totalAvgSpeed;
	
	private Double totalCalorie;
	
	public static CrewTotalRecordDto of(CrewTotalRecordEntity crewTotalRecord) {
		return new CrewTotalRecordDtoBuilder()
				.totalRecordSeq(crewTotalRecord.getTotalRecoredSeq())
				.totalCalorie(crewTotalRecord.getTotalCalorie())
				.totalDistance(crewTotalRecord.getTotalDistance())
				.totalTime(crewTotalRecord.getTotalTime())
				.totalLongestTime(crewTotalRecord.getTotalLongestTime())
				.totalLongestDistance(crewTotalRecord.getTotalLongestDistance())
				.totalAvgSpeed(crewTotalRecord.getTotalAvgSpeed())
				.build();
	}
}