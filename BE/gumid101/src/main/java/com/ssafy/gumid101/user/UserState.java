package com.ssafy.gumid101.user;

public enum UserState {


	
	OK("정상 유저"),
	DELETED("탈퇴한 유저"),
	DENIED("신고되서 처리된 유저");
	
	private String detail;
	public String getDetail() {
		return detail;
	}
	
	 UserState(String detail) {
		this.detail = detail;
	}
}
