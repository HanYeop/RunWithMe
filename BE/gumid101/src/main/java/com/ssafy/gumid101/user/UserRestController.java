package com.ssafy.gumid101.user;

import java.security.Principal;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@Api(tags   = "유저 컨트롤러")
public class UserRestController {

	private final JwtUtilsService jwtUtilService;
	private final UserService userService;

	/**
	 * 닉네임 중복체크
	 * @param nickname
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(code  =200,value =   "닉네임 중복체크")
	@GetMapping("/check/{nickname}")
	public ResponseEntity<?> checkDupNickName(@ApiParam("중복체크할 닉네임") @PathVariable(required = true) String nickname) throws Exception {

		int count = userService.checkDupNickname(nickname);

		ResponseFrame<Integer> responseFrame = new ResponseFrame<>();

		responseFrame.setMsg(String.format("동일 닉네임 갯수 : %d" , count));
		responseFrame.setCount(count);
		responseFrame.setData(count);
		responseFrame.setIsSuccess(true);
		
		return new ResponseEntity<>(responseFrame, HttpStatus.OK);

	}

	/**
	 * 권한이 ROLE_TEMP 여야만 접근가능
	 * 초기 프로필 설정
	 * @param userDto
	 * @return
	 * @throws DuplicateException
	 */
	@ApiOperation(code  =200,value =   "초기 프로필 설정 / 회원가입")
	@PostMapping("/profile")
	public ResponseEntity<?> setMyProfile(@RequestBody UserDto userDto,@ApiIgnore BindingResult result) throws Exception {

		if(result.hasErrors()) {
			result.getAllErrors();
		}
		
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
			responseMap.setIsSuccess(false);
			responseMap.setData(dataMap);
			responseMap.setMsg("초기 프로필/회원 가입 실패");
		} else {
			String token = jwtUtilService.createToken(savedDto);
			dataMap.put(JwtProperties.JWT_ACESS_NAME, token);
			dataMap.put("user", savedDto);
			responseMap.setData(dataMap);
			responseMap.setCount(1);
			responseMap.setIsSuccess(true);
			responseMap.setMsg("초기 프로필 설정/회원 가입 성공");
		}

		return new ResponseEntity<>(responseMap, httpStatus);
	}

	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity<?> duplicationExceptionHandle(DuplicateException de) {
		ResponseFrame<String> responseFrame = new ResponseFrame<String>();

		responseFrame.setCount(1);
		responseFrame.setIsSuccess(false);
		responseFrame.setData(de.getMessage());

		return new ResponseEntity<>(responseFrame, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> catchAllException(Exception e)
	{
		ResponseFrame<String> responseFrame = new ResponseFrame<String>();

		responseFrame.setCount(0);
		responseFrame.setIsSuccess(false);
		responseFrame.setData(e.getMessage());

		return new ResponseEntity<>(responseFrame, HttpStatus.BAD_REQUEST);
	}
	
}
