package com.ssafy.gumid101.dailycheck;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.res.ResponseFrame;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/dailycheck")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "출석체크 컨트롤러")
public class DailyCheckRestController {
	
	private final DailyCheckService dailyServ;
	
	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}
	
	@ApiOperation("출석하기")
	@PostMapping("/today")
	public ResponseEntity<?> dailyChecking() throws Exception{
		UserDto userDto = loadUserFromToken();
		Boolean result = dailyServ.dailyChecking(userDto.getUserSeq());
		return new ResponseEntity<>(new ResponseFrame<>(true, result, 1, "출석체크에 성공했습니다."), HttpStatus.OK);
	}
	
	@ApiOperation("출석 가능한지 확인")
	@GetMapping("/today")
	public ResponseEntity<?> getIsCheckable() throws Exception{
		UserDto userDto = loadUserFromToken();
		Boolean result = dailyServ.isCheckable(userDto.getUserSeq());
		return new ResponseEntity<>(new ResponseFrame<>(true, result, 1, "출석체크가 가능합니다."), HttpStatus.OK);
	}
	
	@ApiOperation("유저의 해당 년, 월 출석일수 확인")
	@GetMapping("/{userSeq}/{year}/{month}")
	public ResponseEntity<?> getMonthlyCheck(@PathVariable(name = "userSeq") Long userSeq, @PathVariable Long year, @PathVariable Long month) throws Exception{
		Integer result = dailyServ.monthResult(userSeq, year, month);
		return new ResponseEntity<>(new ResponseFrame<>(true, result, result, "해당 월 출석체크 횟수를 반환했습니다."), HttpStatus.OK);
	}

	
	
}
