package com.ssafy.gumid101.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.dto.RecordParamsDto;

@RestController
@RequestMapping("/my-activity")
public class MyActivityRestController {

	private UserService userService;

	@GetMapping("/profile")
	public ResponseEntity<?> getMyProfile() {
		return null;
	}


	
	@PatchMapping("/profile")
	public ResponseEntity<?> editMyProfile() {
		return null;
	}
	
	/**
	 * 기록 하나하나가 아닌 누적 수치를 반환
	 * @return
	 */
	@GetMapping("/total-activity")
	public ResponseEntity<?> getMyTotalRecord(){
		return null;
	}
	
	/**
	 * 기록 하나하나의 정보가 담긴 리스트를 반환
	 * @param size
	 * @param offset
	 * @param year
	 * @param month
	 * @return
	 */
	@GetMapping("/activity")
	public ResponseEntity<?> getMyTotalRecordList(RecordParamsDto params) {
		/**	
		 * int값은 안 들어올 때 0으로 들어오는것으로 알고, size, offset, year, month는 검색 조건이 있다면 0이 아닌 값이므로
		 * 0이 들어왔을 떼 예외적인 처리를 해야함.
		 */
		
		return null;
	}

	@GetMapping("/rewards")
	public ResponseEntity<?> getMyAchieve() {
		return null;
	}
	
	@GetMapping("/boards")
	public ResponseEntity<?> getMyBoards(@RequestParam(required = false) int size, @RequestParam(required = false) int offset) {
		/**
		 * int값은 안 들어올 때 0으로 들어오는것으로 알고, size, offset은 검색 조건이 있다면 0이 아닌 값이므로
		 * 0이 들어왔을 떼 예외적인 처리를 해야함.
		 */
		return null;
	}
	

}
