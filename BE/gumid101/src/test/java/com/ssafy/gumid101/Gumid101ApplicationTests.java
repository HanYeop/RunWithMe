package com.ssafy.gumid101;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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

//jdk 8은 junit 4를 사용해야한다.
@RunWith(SpringRunner.class)
@SpringBootTest
class Gumid101ApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JwtUtilsService jwtService;

	@Test
	void contextLoads() {

	}

	@Test
	void initailProfile() throws Exception {
		// given
		UserDto userDto = new UserDto();
		userDto.setEmail("qwe@naver.com");
		userDto.setRole(Role.TEMP);
		jwtService = new JwtUtilsService();
		String token = jwtService.createToken(userDto);

		mvc.perform(post("/api/user/profile").header(JwtProperties.JWT_ACESS_NAME, token)).andDo(print());

	}
	
	

}
