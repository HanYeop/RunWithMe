package com.ssafy.gumid101.achievement;

import java.util.List;

import com.ssafy.gumid101.dto.AchievementDto;
import com.ssafy.gumid101.res.AchieveCompleteDto;

public interface AchievementService{

	List<AchieveCompleteDto> getUserAchievement(Long userSeq) throws Exception;

	List<AchievementDto> getAchieveList() throws Exception;

	void checkAchievement(String acName, AchieveType acType, Double acValue);

}
