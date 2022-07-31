package com.ssafy.gumid101;

import java.util.TimeZone;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
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
