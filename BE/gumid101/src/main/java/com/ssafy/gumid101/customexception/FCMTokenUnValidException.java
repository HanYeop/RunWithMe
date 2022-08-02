package com.ssafy.gumid101.customexception;

public class FCMTokenUnValidException extends Exception {

	private String errorResponse;
	public FCMTokenUnValidException(String msg) {
		super(msg);
	}
	public FCMTokenUnValidException(String msg,String errorResponse) {
		super(msg);
		this.errorResponse = errorResponse;
	}
	
	public String getErrorResponse() {
		return this.errorResponse;
	}
	
}
