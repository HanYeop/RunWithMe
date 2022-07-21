package com.ssafy.gumid101.user;

import java.security.Principal;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.customexception.DuplicateException;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.jwt.JwtProperties;
import com.ssafy.gumid101.jwt.JwtUtilsService;
import com.ssafy.gumid101.res.ResponseFrame;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserRestController {

	private final JwtUtilsService jwtUtilService;
	private final UserService userService;

	/**
	 * 닉네임 중복체크
	 * @param nickname
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/check/{nickname}")
	public ResponseEntity<?> checkDupNickName(@PathVariable String nickname) throws Exception {

		int count = userService.checkDupNickname(nickname);

		ResponseFrame<Integer> responseFrame = new ResponseFrame<>();

		responseFrame.setCount(count);
		responseFrame.setData(count);
		responseFrame.setSuccess(true);
		
		return new ResponseEntity<>(responseFrame, HttpStatus.OK);

	}

	/**
	 * 권한이 ROLE_TEMP 여야만 접근가능
	 * 초기 프로필 설정
	 * @param userDto
	 * @return
	 * @throws DuplicateException
	 */
	@PostMapping("/profile")
	public ResponseEntity<?> setMyProfile(@RequestBody UserDto userDto) throws Exception {

		log.debug("초기 프로필 설정 진입 : 몸무게:{},키 : {}, 닉네임 :{}", userDto.getWeight(), userDto.getHeight(),
				userDto.getNickName());

		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		// Authentication에서 email을 갖기 위함

		userDto.setEmail(tokenUser.getEmail());

		UserDto savedDto = userService.setMyProfile(userDto);

		ResponseFrame<Map<String, Object>> responseMap = new ResponseFrame<Map<String, Object>>();

		HashMap<String, Object> dataMap = new HashMap<String, Object>();

		HttpStatus httpStatus = HttpStatus.OK;

		if (savedDto == null) {
			httpStatus = HttpStatus.CONFLICT;
			dataMap.put(JwtProperties.JWT_ACESS_NAME, "");
			dataMap.put("user", savedDto);
			responseMap.setCount(0);
			responseMap.setSuccess(false);
			responseMap.setData(dataMap);
		} else {
			String token = jwtUtilService.createToken(savedDto);
			dataMap.put(JwtProperties.JWT_ACESS_NAME, token);
			dataMap.put("user", savedDto);
			responseMap.setData(dataMap);
			responseMap.setCount(1);
			responseMap.setSuccess(true);
		}

		return new ResponseEntity<>(responseMap, httpStatus);
	}

	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity<?> duplicationExceptionHandle(DuplicateException de) {
		ResponseFrame<String> responseFrame = new ResponseFrame<String>();

		responseFrame.setCount(1);
		responseFrame.setSuccess(false);
		responseFrame.setData(de.getMessage());

		return new ResponseEntity<>(responseFrame, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> catchAllException(Exception e)
	{
		ResponseFrame<String> responseFrame = new ResponseFrame<String>();

		responseFrame.setCount(1);
		responseFrame.setSuccess(false);
		responseFrame.setData(e.getMessage());

		return new ResponseEntity<>(responseFrame, HttpStatus.BAD_REQUEST);
	}
	
}
