package com.ssafy.gumid101;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.jwt.JwtProperties;
import com.ssafy.gumid101.jwt.JwtUtilsService;
import com.ssafy.gumid101.user.Role;

@SpringBootTest
class Gumid101ApplicationTests {

	@Test
	void contextLoads() {

	}

}
