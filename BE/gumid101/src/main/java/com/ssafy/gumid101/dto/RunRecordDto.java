package com.ssafy.gumid101.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.gumid101.entity.RunRecordEntity;

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
public class RunRecordDto implements Serializable {

	private Long runRecordSeq;
	
	private Long runImageSeq;

	@ApiParam(value = "달리기 시작 년월일시분초. (yyyy-MM-dd HH:mm:ss)", required = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime runRecordStartTime;
	
	@ApiParam(value = "달리기 종료 년월일시분초. (yyyy-MM-dd HH:mm:ss)", required = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime runRecordEndTime;
	
	@ApiParam(value = "달린 시간 (초단위)", required = true)
	private Integer runRecordRunningTime;

	@ApiParam(value = "달린 거리 (미터단위)", required = true)
	private Integer runRecordRunningDistance;
	
	@ApiModelProperty(hidden = true)
	@ApiParam(value = "평균 속도 km/h인데 입력받지 않고 서버에서 계산해서 저장함")
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
		
		if(runRecord == null)
			return null;
		
		return new RunRecordDtoBuilder().runRecordSeq(runRecord.getRunRecordSeq())
				.runRecordStartTime(runRecord.getRunRecordStartTime())
				.runRecordEndTime(runRecord.getRunRecordEndTime())
				.runRecordRunningTime(runRecord.getRunRecordRunningTime())
				.runRecordRunningDistance(runRecord.getRunRecordRunningDistance())
				.runRecordRunningAvgSpeed(runRecord.getRunRecordRunningTime() == null || runRecord.getRunRecordRunningTime() == 0 ? 0 :
						3.6 * runRecord.getRunRecordRunningDistance() / runRecord.getRunRecordRunningTime())
				.runRecordRunningCalorie(runRecord.getRunRecordCalorie())
				.runRecordRunningLat(runRecord.getRunRecordLat())
				.runRecordRunningLng(runRecord.getRunRecordLng())
				.runRecordRunningCompleteYN(runRecord.getRunRecordCompleteYN())
				.runImageSeq(runRecord.getImageFile() == null ? 0 : runRecord.getImageFile().getImgSeq())
				.build();
	}
}