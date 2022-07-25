package com.ssafy.gumid101.crew;

public enum CrewGoalType {

	DISTANCE("distance"),TIME("time");
	private final String key;
	CrewGoalType(String key){
		this.key = key;
	};
	
	public String getKey() {
		return key;
	}
}
