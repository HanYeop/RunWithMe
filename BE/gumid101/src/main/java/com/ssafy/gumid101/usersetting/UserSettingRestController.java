package com.ssafy.gumid101.usersetting;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.dto.UserSettingDto;
import com.ssafy.gumid101.res.ResponseFrame;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/usersetting")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "유저 환경설정 컨트롤러")
public class UserSettingRestController {
	
	private final UserSettingService settingServ;
	
	@ApiParam(value = "유저 환경정보 세팅 반환")
	@GetMapping(value = "/{userSeq}")
	public ResponseEntity<?> getUserSetting(@PathVariable Long userSeq) throws Exception{
		return new ResponseEntity<>(new ResponseFrame<>(true, settingServ.getUserSetting(userSeq), 1, "유저 환경설정을 반환합니다."), HttpStatus.OK);
	}
	
	@ApiParam(value = "유저 환경정보 세팅 (넣은 정보만 변경, null인 정보는 유지)")
	@PostMapping(value = "/{userSeq}")
	public ResponseEntity<?> setUserSetting(@PathVariable Long userSeq, @RequestBody UserSettingDto userSettingDto) throws Exception{
		return new ResponseEntity<>(new ResponseFrame<>(true, settingServ.setUserSetting(userSeq, userSettingDto), 1, "유저 환경설정에 성공했습니다."), HttpStatus.OK);
	}
	
	

}
