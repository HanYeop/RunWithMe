package com.ssafy.gumid101.achievement;

import java.util.List;

import com.ssafy.gumid101.dto.AchievementDto;
import com.ssafy.gumid101.res.MyAchieveCompleteDto;

public interface AchievementService{

	List<MyAchieveCompleteDto> getUserAchievement(Long userSeq) throws Exception;

	List<AchievementDto> getAchieveList() throws Exception;

}
