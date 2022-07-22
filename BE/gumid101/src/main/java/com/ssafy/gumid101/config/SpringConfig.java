package com.ssafy.gumid101.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.fasterxml.jackson.databind.ObjectMapper;

@EnableJpaAuditing
public class SpringConfig {

	//ObjectMapper 는 쓰레드 세이프하다. 하위버전에서 락으로 인해 성능이슈가 있었으나 해결됨
	@Bean 
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
