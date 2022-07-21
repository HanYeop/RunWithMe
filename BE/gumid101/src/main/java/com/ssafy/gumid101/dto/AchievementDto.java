package com.ssafy.gumid101.dto;

import java.io.Serializable;

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

	private long achieveSeq;
	
	private String achieveName;
	
	private String achieveType;
	
	private double achieveValue;
	
	public static AchievementDto of(AchievementEntity achieve) {
		return new AchievementDtoBuilder()
				.achieveSeq(achieve.getAchiveSeq())
				.achieveName(achieve.getAchieveName())
				.achieveType(achieve.getAchieveType())
				.achieveValue(achieve.getAchiveValue())
				.build();
	}
}