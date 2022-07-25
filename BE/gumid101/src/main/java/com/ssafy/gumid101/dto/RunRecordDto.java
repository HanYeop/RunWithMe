package com.ssafy.gumid101.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.ssafy.gumid101.entity.RunRecordEntity;

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
public class RunRecordDto implements Serializable {

	private Long runRecordSeq;

	@ApiParam(value = "달리기 시작 년월일시분초. (yyyy-MM-dd HH:mm:ss)", required = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime runRecordStartTime;
	
	@ApiParam(value = "달리기 종료 년월일시분초. (yyyy-MM-dd HH:mm:ss)", required = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime runRecordEndTime;
	
	@ApiParam(value = "달린 시간 (초단위)", required = true)
	private Integer runRecordRunningTime;

	@ApiParam(value = "달린 거리 (미터단위)", required = true)
	private Integer runRecordRunningDistance;
	
	@ApiParam(value = "평균 속도 (km/h로 할지 m/s로 할지 나중에 결정합시다.)", required = true)
	private Double runRecordRunningAvgSpeed;
	
	@ApiParam(value = "소비 칼로리 (Kcal)")
	private Double runRecordRunningCalorie;
	
	@ApiParam(value = "시작점 위도")
	private Double runRecordRunningLat;
	
	@ApiParam(value = "시작점 경도")
	private Double runRecordRunningLng;
	
	@ApiParam(value = "목표달성 여부")
	private String runRecordRunningCompleteYN;
	
	public static RunRecordDto of(RunRecordEntity runRecord) {
		return new RunRecordDtoBuilder().runRecordSeq(runRecord.getRunRecordSeq())
				.runRecordStartTime(runRecord.getRunRecordStartTime())
				.runRecordEndTime(runRecord.getRunRecordEndTime())
				.runRecordRunningTime(runRecord.getRunRecordRunningTime())
				.runRecordRunningDistance(runRecord.getRunRecordRunningDistance())
				.runRecordRunningAvgSpeed(runRecord.getRunRecordAvgSpeed())
				.runRecordRunningCalorie(runRecord.getRunRecordCalorie())
				.runRecordRunningLat(runRecord.getRunRecordLat())
				.runRecordRunningLng(runRecord.getRunRecordLng())
				.runRecordRunningCompleteYN(runRecord.getRunRecordCompleteYN())
				.build();
	}
}