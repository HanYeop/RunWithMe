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
	private Long size; //오프셋으로 부터 보여줄 갯수
	private Long offset; //0~떨어진 정도
	private String title; //크루 이름 검색
	private String startDay; //시작일
	private String endDay; //종료일
	private String startTime; //시작 시간
	private String endTime; //끝나는 시간
	private Integer pointMin; //포인트 최소 (포함)
	private Integer pointMax; //포인트 최대 (포함)
	private String purposeType; //perposeType distance,time
	private Integer purposeMinValue; //목표 최소 (포함)
	private Integer purposeMaxValue; //목표 최대 ( 포함 )
	private Integer goalMinDay; //1주일에 행해야하는 횟수 최소
	private Integer goalMaxDay; //1주일에 행해야하는 횟수 최대
}