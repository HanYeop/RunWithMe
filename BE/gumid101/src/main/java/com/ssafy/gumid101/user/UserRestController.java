package com.ssafy.gumid101.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.ssafy.gumid101.util.Nickname;

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
	
	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}

	
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
	@ApiOperation(code  =200,value =   "초기 프로필 설정 / 회원가입")
	@PostMapping("/profile")
	public ResponseEntity<?> setMyProfile(@RequestBody UserDto userDto,@ApiIgnore BindingResult result) throws Exception {

		if(result.hasErrors()) {
			log.warn(result.getAllErrors().toString()); ;
		}
		
		log.debug("초기 프로필 설정 진입 : 몸무게:{},키 : {}, 닉네임 :{}", userDto.getWeight(), userDto.getHeight(),
				userDto.getNickName());

		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		// Authentication에서 email을 갖기 위함

		userDto.setEmail(tokenUser.getEmail());

		ResponseFrame<Map<String, Object>> responseMap = new ResponseFrame<Map<String, Object>>();
		
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		
		HttpStatus httpStatus = HttpStatus.OK;

		if (!Nickname.nickOk(userDto.getNickName())) {
			responseMap.setData(dataMap);
			responseMap.setCount(0);
			responseMap.setSuccess(false);
			responseMap.setMsg("닉네임을 입력하지 않았거나 규칙을 위반했습니다.");
			return new ResponseEntity<>(responseMap, httpStatus);
		}
		UserDto savedDto = userService.setMyProfile(userDto);


		if (savedDto == null) {
			httpStatus = HttpStatus.OK;
			dataMap.put(JwtProperties.JWT_ACESS_NAME, "");
			dataMap.put("userSeq", -1);
			responseMap.setCount(0);
			responseMap.setSuccess(false);
			responseMap.setData(dataMap);
			responseMap.setMsg("초기 프로필/회원 가입 실패");
		} else {
			String token = jwtUtilService.createToken(savedDto);
			dataMap.put(JwtProperties.JWT_ACESS_NAME, token);
			dataMap.put("userSeq", savedDto.getUserSeq());
			responseMap.setData(dataMap);
			responseMap.setCount(1);
			responseMap.setSuccess(true);
			responseMap.setMsg("초기 프로필 설정/회원 가입 성공");
		}

		return new ResponseEntity<>(responseMap, httpStatus);
	}
	
	@ApiOperation("fcm 토큰 설정")
	@PostMapping("/fcm-token")
	public ResponseEntity<?> setMyFcmToken(@ApiParam("{fcmToken:\"값\"}") @RequestBody Map<String,String> body)throws Exception{
		
		
		UserDto userDto= loadUserFromToken();
		log.info(body.get("fcmToken"));
		boolean result =  userService.setUserFcmToken(userDto.getUserSeq(),body.get("fcmToken"));
		

		return new ResponseEntity<>(ResponseFrame.of(result, "FCM 토큰이 정상적으로 등록되었습니다."), HttpStatus.OK);
	}
	
	@ApiOperation("fcm 토큰 삭제")
	@DeleteMapping("/fcm-token")
	public ResponseEntity<?> deleteMyFcmToken() throws Exception{
		
		UserDto userDto= loadUserFromToken();
		boolean result = userService.deleteUserFcmToken(userDto.getUserSeq());
		
		return new ResponseEntity<>(ResponseFrame.of(result, "FCM 토큰이 정상적으로 삭제되었습니다."), HttpStatus.OK);
	}

	@DeleteMapping("/exit")
	public ResponseEntity<?> deleteMyAccount() throws Exception{
		
		UserDto userDto =  loadUserFromToken();
		
		boolean result = userService.deleteMyAccount(userDto.getUserSeq());
		
		String msg = result == true ? "정상적으로 회원탈퇴 되었습니다." :"회원탈퇴를 실패하였습니다. 다시 시도해주세요.";
		return new ResponseEntity<>(ResponseFrame.of(result,msg), HttpStatus.OK);
	}
	
}
