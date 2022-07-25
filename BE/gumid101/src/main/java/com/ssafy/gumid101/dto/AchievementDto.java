package com.ssafy.gumid101.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.gumid101.entity.AchievementEntity;

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
public class AchievementDto implements Serializable {

	@ApiParam(value = "업적 번호")
	private Long achieveSeq;

	@ApiParam(value = "업적 이름")
	private String achieveName;

	@ApiParam(value = "업적 형식")
	private String achieveType;

	@ApiParam(value = "업적 목표값")
	private Double achieveValue;

	@ApiParam(value = "업적 달성 시점. (HH:mm:ss)")
	@DateTimeFormat(pattern = "HH:mm:ss")
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalDateTime achieveRegTime;
	
	public static AchievementDto of(AchievementEntity achieve) {
		return new AchievementDtoBuilder()
				.achieveSeq(achieve.getAchiveSeq())
				.achieveName(achieve.getAchieveName())
				.achieveType(achieve.getAchieveType())
				.achieveValue(achieve.getAchiveValue())
				.achieveRegTime(achieve.getAchieveRegTime())
				.build();
	}
}