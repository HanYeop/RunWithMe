package com.ssafy.gumid101.achievement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AchieveType {
    RUNCOUNT("runCount"),
    TOTALDISTANCE("totalDistance"),
    TOTALTIME("totalTime"),
	DISTANCE("distance"),
	TIME("time");
    private final String type;
}