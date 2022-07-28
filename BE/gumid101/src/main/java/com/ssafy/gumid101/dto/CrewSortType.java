package com.ssafy.gumid101.dto;

public enum CrewSortType {

	REG_RECENT("등록일 최신으로 정렬합니다."),STARTDATE_RECENT("시작일이 얼마남지 않은 순으로 정렬합니다.");
	
	CrewSortType(String desc) {
		this.desc = desc;
	}

	private final String desc;
	public String getDesc() {
		return desc;
	}
}
