package com.ssafy.gumid101.res;

import java.time.LocalDateTime;

import com.ssafy.gumid101.dto.AchievementDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MyAchieveCompleteDto {
	private final AchievementDto achievementDto;
	private final LocalDateTime regTime;
}
