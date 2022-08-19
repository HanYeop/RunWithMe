package com.ssafy.gumid101.customexception;

public class RequestAlreadyProcessingException extends Exception {

	public RequestAlreadyProcessingException(String msg) {
		super(msg);
	}
}
