package com.ssafy.gumid101.user;

import java.util.List;
import java.util.Map;

import org.apache.http.annotation.Experimental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.customexception.ThirdPartyException;
import com.ssafy.gumid101.dto.CrewBoardDto;
import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.req.ProfileEditDto;
import com.ssafy.gumid101.res.ResponseFrame;
import com.ssafy.gumid101.res.UserFileDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-activity")
@Api(tags = "내정보 관련 컨트롤러")
public class MyActivityRestController {

	private final UserService userService;

	/**
	 * 토큰으로 부터 유저 DTO 로드
	 * 
	 * @return
	 */
	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}

	/**
	 * 자신의 프로필 조회
	 * 
	 * @return
	 */
	@ApiOperation(value = "자신의 회원 정보 조회/프로필 조회")
	@GetMapping("/profile")
	public ResponseEntity<?> getMyProfile() throws Exception {

		UserDto userDto = loadUserFromToken();

		UserDto resUserDto = userService.getUserProfileById(userDto.getUserSeq());

		ResponseFrame<UserDto> resFrame = new ResponseFrame<UserDto>();

		resFrame.setCount(resUserDto == null ? 0 : 1);
		resFrame.setSuccess(resUserDto == null ? false : true);
		resFrame.setData(resUserDto);

		return new ResponseEntity<>(resFrame, resUserDto != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "자신의 프로필 수정")
	@PostMapping("/profile")
	public ResponseEntity<?> editMyProfile(@ModelAttribute ProfileEditDto profile,
			@RequestPart(value = "imgFile") MultipartFile imgFile) throws Exception {

		UserDto userDto = loadUserFromToken();
		userDto.setWeight(profile.getWeight());
		userDto.setHeight(profile.getHeight());
		userDto.setNickName(profile.getNickName());
		
		UserFileDto userFileDto= userService.editMyProfile(userDto,imgFile);
		
		ResponseFrame<UserFileDto> res = new ResponseFrame<>();
		
		res.setCount(userFileDto == null ?  0 : 1);
		res.setData(userFileDto);
		res.setSuccess(userFileDto == null ?  false: true);
		
		return new ResponseEntity<>(res,HttpStatus.OK);
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

	/**
	 *
	 * @return
	 */
	@GetMapping("/rewards")
	public ResponseEntity<?> getMyAchieve() {
		return null;
	}

	/**
	 * int값은 안 들어올 때 0으로 들어오는것으로 알고, size, offset은 검색 조건이 있다면 0이 아닌 값이므로 0이 들어왔을 떼
	 * 예외적인 처리를 해야함.
	 * @throws Exception 
	 */
	@GetMapping("/boards") //안쓴 거는 null로 받기위해 참조형으로 파라메터를 받음
	public ResponseEntity<?> getMyBoards(@RequestParam(required = false,name = "size") Long size,
			@RequestParam(required = false,name = "offset") Long offset) throws Exception {
		UserDto userDto = loadUserFromToken();
		
		List<CrewBoardDto> myBoardList =  userService.getMyBoards(userDto.getUserSeq(),size,offset);
		
		ResponseFrame<?> res = ResponseFrame.of(myBoardList,myBoardList.size(),"자신이 쓴 글을 반환합니다.");
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	/**
	 * 우리 자체의 오류가 아니라 , S3를 사용하면서 난 오류이다.
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ThirdPartyException.class)
	public ResponseEntity<?> thirdParthExceptionHandle(ThirdPartyException e){
		
		ResponseFrame<?> res = new ResponseFrame<>();
		
		res.setCount(0);
		res.setData(null);
		res.setSuccess(false);
		
		return new ResponseEntity<>(res,HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
}
