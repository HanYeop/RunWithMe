package com.ssafy.gumid101.achievement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AchieveType {
    RUNCOUNT("runcount"),
    TOTALDISTANCE("totaldistance"),
    TOTALTIME("totaltime"),
	DISTANCE("distance"),
	TIME("time");
    private final String type;
}