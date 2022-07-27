package com.ssafy.gumid101.achievement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.dto.AchievementDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.res.MyAchieveCompleteDto;
import com.ssafy.gumid101.res.ResponseFrame;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/achieve")
@RequiredArgsConstructor
@Api(tags = "업적 컨트롤러")
@Slf4j
public class AchievementRestController {
	
	private final AchievementService achievementService;
	
	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}
	
	@ApiOperation(value="전체 업적 목록 조회")
	@GetMapping("/list")
	public ResponseEntity<?> getAchieveList() throws Exception{
		
		UserDto userDto = loadUserFromToken();
		HttpStatus httpStatus = HttpStatus.OK;
		ResponseFrame<List<AchievementDto>> responseMap = new ResponseFrame<>();
		
		
		List<AchievementDto> achieveDtoList = null;
		try {
			achieveDtoList = achievementService.getAchieveList();
		}catch (Exception e) {
			httpStatus = HttpStatus.CONFLICT;
			responseMap.setCount(0);
			responseMap.setSuccess(false);
			responseMap.setMsg(e.getMessage());
		}
		
		if (achieveDtoList != null) {
			responseMap.setCount(achieveDtoList.size());
			responseMap.setSuccess(true);
			responseMap.setMsg("업적 목록 반환에 성공했습니다.");
		}
		responseMap.setData(achieveDtoList);
		return new ResponseEntity<>(responseMap, httpStatus);
	}

	@ApiOperation(value="내가 달성한 업적 조회")
	@GetMapping("/my")
	public ResponseEntity<?> getMyAchieve() throws Exception{

		UserDto userDto = loadUserFromToken();
		HttpStatus httpStatus = HttpStatus.OK;
		ResponseFrame<List<MyAchieveCompleteDto>> responseMap = new ResponseFrame<>();
		
		
		List<MyAchieveCompleteDto> achieveDtoList = null;
		try {
			achieveDtoList = achievementService.getUserAchievement(userDto.getUserSeq());
		}catch (Exception e) {
			httpStatus = HttpStatus.CONFLICT;
			responseMap.setCount(0);
			responseMap.setSuccess(false);
			responseMap.setMsg(e.getMessage());
		}
		
		if (achieveDtoList != null) {
			responseMap.setCount(achieveDtoList.size());
			responseMap.setSuccess(true);
			responseMap.setMsg("달성한 업적 반환에 성공했습니다.");
		}
		responseMap.setData(achieveDtoList);
		return new ResponseEntity<>(responseMap, httpStatus);
	}
}
