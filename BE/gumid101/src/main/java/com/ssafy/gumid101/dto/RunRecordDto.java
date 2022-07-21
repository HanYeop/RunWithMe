package com.ssafy.gumid101.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.ssafy.gumid101.entity.RunRecordEntity;

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

	private long runRecordSeq;

	private LocalDateTime runRecordStartTime;
	
	private LocalDateTime runRecordEndTime;
	
	private int runRecordRunningTime;

	private double runRecordRunningDistance;
	
	private double runRecordRunningAvgSpeed;
	
	private double runRecordRunningCalorie;
	
	private double runRecordRunningLat;
	
	private double runRecordRunningLng;
	
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