package com.ssafy.gumid101.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.ssafy.gumid101.entity.AchievementEntity;

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

	private Long achieveSeq;
	
	private String achieveName;
	
	private String achieveType;
	
	private Double achieveValue;
	
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