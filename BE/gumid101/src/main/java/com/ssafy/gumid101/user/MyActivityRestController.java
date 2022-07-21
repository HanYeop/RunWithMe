package com.ssafy.gumid101.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.res.ResponseFrame;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-activity")
public class MyActivityRestController {

	private final UserService userService;

	/**
	 * 자신의 프로필 조회
	 * 
	 * @return
	 */
	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}

	@GetMapping("/profile")
	public ResponseEntity<?> getMyProfile() throws Exception {

		UserDto userDto = loadUserFromToken();
		
		UserDto resUserDto = userService.getUserProfileById(userDto.getId());
		
		ResponseFrame<UserDto> resFrame = new ResponseFrame<UserDto>();
		
		resFrame.setCount(resUserDto == null ? 0:1);
		resFrame.setSuccess(resUserDto == null ? false:true);
		resFrame.setData(resUserDto);

		return new ResponseEntity<>(resFrame, resUserDto != null ?  HttpStatus.OK:HttpStatus.BAD_REQUEST);
	}

	@PatchMapping("/profile")
	public ResponseEntity<?> editMyProfile() {
		return null;
	}

	/**
	 * 기록 하나하나가 아닌 누적 수치를 반환
	 * 
	 * @return
	 */
	@GetMapping("/total-activity")
	public ResponseEntity<?> getMyTotalRecord() {
		return null;
	}

	/**
	 * 기록 하나하나의 정보가 담긴 리스트를 반환
	 * 
	 * @param size
	 * @param offset
	 * @param year
	 * @param month
	 * @return
	 */
	@GetMapping("/activity")
	public ResponseEntity<?> getMyTotalRecordList(RecordParamsDto params) {
		/**
		 * int값은 안 들어올 때 0으로 들어오는것으로 알고, size, offset, year, month는 검색 조건이 있다면 0이 아닌
		 * 값이므로 0이 들어왔을 떼 예외적인 처리를 해야함.
		 */

		return null;
	}

	@GetMapping("/rewards")
	public ResponseEntity<?> getMyAchieve() {
		return null;
	}

	@GetMapping("/boards")
	public ResponseEntity<?> getMyBoards(@RequestParam(required = false) int size,
			@RequestParam(required = false) int offset) {
		/**
		 * int값은 안 들어올 때 0으로 들어오는것으로 알고, size, offset은 검색 조건이 있다면 0이 아닌 값이므로 0이 들어왔을 떼
		 * 예외적인 처리를 해야함.
		 */
		return null;
	}

}
