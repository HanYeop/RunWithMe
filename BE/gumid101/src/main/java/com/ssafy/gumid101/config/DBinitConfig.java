package com.ssafy.gumid101.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.ssafy.gumid101.achievement.AchieveType;
import com.ssafy.gumid101.achievement.AchievementService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DBinitConfig {
	
	private final AchievementService acServ;
	
	@PostConstruct
	public void AchieveInit() {
		acServ.checkAchievement("첫 러닝", AchieveType.RUNCOUNT, 1.0);
		acServ.checkAchievement("10 러닝", AchieveType.RUNCOUNT, 10.0);
		acServ.checkAchievement("100 러닝", AchieveType.RUNCOUNT, 100.0);
		acServ.checkAchievement("1000 러닝", AchieveType.RUNCOUNT, 1000.0);
		acServ.checkAchievement("10h", AchieveType.TOTALTIME, 10.0);
		acServ.checkAchievement("100h", AchieveType.TOTALTIME, 100.0);
		acServ.checkAchievement("1000h", AchieveType.TOTALTIME, 1000.0);
		acServ.checkAchievement("10000h", AchieveType.TOTALTIME, 10000.0);
		acServ.checkAchievement("10km", AchieveType.TOTALDISTANCE, 10.0);
		acServ.checkAchievement("100km", AchieveType.TOTALDISTANCE, 100.0);
		acServ.checkAchievement("1000km", AchieveType.TOTALDISTANCE, 1000.0);
		acServ.checkAchievement("10000km", AchieveType.TOTALDISTANCE, 10000.0);
	}

}
