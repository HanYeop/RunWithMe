package com.ssafy.gumid101.imgfile;

import lombok.Getter;

@Getter
public enum ImageDirectory {

	PROFILE("profile"),CREW_LOGO("crewlogo"),RUN_RECORD("runrecord"),CREW_BOARD("crewboard"),RECOMMEND("recommend"),COMPETITION_LOGO("competitionlogo");
	ImageDirectory(String path) {
		this.path= path;
		
	}

	private final String path;
}
