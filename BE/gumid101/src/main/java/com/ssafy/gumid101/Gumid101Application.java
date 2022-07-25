package com.ssafy.gumid101;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Gumid101Application {

	public static void main(String[] args) {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");

		SpringApplication.run(Gumid101Application.class, args);
	}

}
