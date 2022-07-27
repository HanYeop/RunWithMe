package com.ssafy.gumid101.res;

import java.util.List;

import com.ssafy.gumid101.dto.AchievementDto;
import com.ssafy.gumid101.dto.RunRecordDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RunRecordResultDto {
	RunRecordDto runRecord;
	List<AchievementDto> achievements;
	
}
