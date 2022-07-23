package com.ssafy.gumid101.dto;

import java.io.Serializable;

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
	private Integer size;
	private Integer offset;
	private String title;
	private String startDay;
	private String endDay;
	private String startTime;
	private String endTime;
	private Integer pointMin;
	private Integer pointMax;
	private String purposeType;
	private Integer purposeMinValue;
	private Integer purposeMaxValue;
	private Integer goalMinDays;
	private Integer goalMaxDay;
}