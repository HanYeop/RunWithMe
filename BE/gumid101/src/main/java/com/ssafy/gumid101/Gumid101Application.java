package com.ssafy.gumid101;

import java.time.ZoneId;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Gumid101Application {

	public void setTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
	public static void main(String[] args) {
		
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");

		SpringApplication.run(Gumid101Application.class, args);
	}

}
