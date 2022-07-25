package com.ssafy.gumid101.OAuth;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.jwt.JwtProperties;
import com.ssafy.gumid101.jwt.JwtUtilsService;
import com.ssafy.gumid101.user.Role;
import com.ssafy.gumid101.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private final ObjectMapper mapper;
	private final UserRepository userRepository;
	private final JwtUtilsService jwtUtils;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		log.debug("Principal에서 꺼낸 OAuth2User = {}", oAuth2User);
		String email = oAuth2User.getAttribute("email");
		// 최초 로그인이라면 회원가입 처리를 한다.
		// DB 확인하고 여러가지

		UserEntity user = userRepository.findByEmail(email).orElse(null);

		// 토큰에 담을 정보 ... id,email,role , status
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json,charset=utf-8");
		
		PrintWriter writer = response.getWriter();
		UserDto userDto = null;
		String token = null;
		Map<String, Object> map = new HashMap<>();
		
		if (user != null) {
			userDto = UserDto.of(user);
			token = jwtUtils.createToken(userDto);
			response.setStatus(HttpStatus.OK.value());
			map.put("msg", "정상적 인증으로 토큰을 발급합니다.");
			map.put("isRegistered",true);
			map.put("email",null);
			
		} else {
			userDto = new UserDto();
			userDto.setEmail(email);
			userDto.setRole(Role.TEMP);
			token = jwtUtils.createToken(userDto);
			map.put("msg", "초기 프로필 설정이 필요합니다.");
			map.put("email",userDto.getEmail());
			map.put("isRegistered",false);
			
		}
		
		map.put(JwtProperties.JWT_ACESS_NAME, token);
		writer.append(mapper.writeValueAsString(map));
		writer.flush();


		log.info("토큰 발행 :{}",map.toString());

	}
}
