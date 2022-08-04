package com.ssafy.gumid101.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.gumid101.crew.activity.CrewActivityService;
import com.ssafy.gumid101.dto.CrewTotalRecordDto;
import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.redis.RedisService;
import com.ssafy.gumid101.req.ProfileEditDto;
import com.ssafy.gumid101.res.CrewBoardRes;
import com.ssafy.gumid101.res.ResponseFrame;
import com.ssafy.gumid101.res.UserFileDto;
import com.ssafy.gumid101.util.Nickname;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-activity")
@Api(tags = "내정보 관련 컨트롤러")
public class MyActivityRestController {

	private final UserService userService;
	private final CrewActivityService runService;
	private final ObjectMapper objectMapper; 
	private final RedisService redisServ;
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

		UserFileDto resUserDto = userService.getUserProfileById(userDto.getUserSeq());

		
		if(resUserDto.getImgFileDto() == null) {
			resUserDto.setImgFileDto(ImageFileDto.builder()
					.imgOriginalName("초기")
					.imgSavedName("초기")
					.imgSavedPath("초기")
					.imgSeq(0L).build());
		}
		if(resUserDto.getUser().getFcmToken() == null) {
			resUserDto.getUser().setFcmToken("");
		}
		
		
		ResponseFrame<UserFileDto> resFrame = new ResponseFrame<UserFileDto>();
		resFrame.setCount(resUserDto == null ? 0 : 1);
		resFrame.setSuccess(resUserDto == null ? false : true);
		resFrame.setMsg("회원의 정보를 반환합니다.");
		resFrame.setData(resUserDto);
		return new ResponseEntity<>(resFrame, resUserDto != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "자신의 프로필 수정")
	@PostMapping(value="/profile",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> editMyProfile( @RequestPart ProfileEditDto profile,
			@RequestPart(value = "imgFile",required = false) MultipartFile imgFile) throws Exception {
		UserDto userDto = loadUserFromToken();

		redisServ.getIsUseable(userDto.getUserSeq().toString() + "editProfile", 10);
		
		ProfileEditDto profileEditDto = profile;//objectMapper.readValue(profile, ProfileEditDto.class);

		ResponseFrame<UserFileDto> res = new ResponseFrame<>();

		if (!Nickname.nickOk(profileEditDto.getNickName())) {
			res.setData(null);
			res.setCount(0);
			res.setSuccess(false);
			res.setMsg("닉네임을 입력하지 않았거나 규칙을 위반했습니다.");
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		userDto.setWeight(profileEditDto.getWeight());
		userDto.setHeight(profileEditDto.getHeight());
		userDto.setNickName(profileEditDto.getNickName());
		
		UserFileDto userFileDto= userService.editMyProfile(userDto,imgFile);
		
		
		res.setCount(userFileDto == null ?  0 : 1);
		res.setData(userFileDto);
		res.setSuccess(userFileDto == null ?  false: true);
		
		return new ResponseEntity<>(res,HttpStatus.OK);
		
	}

	/**
	 * 기록 하나하나가 아닌 누적 수치를 반환
	 * 
	 * @return
	 * @throws Exception 
	 */
	@GetMapping("/total-activity")
	public ResponseEntity<?> getMyTotalRecord() throws Exception {
		UserDto userDto = loadUserFromToken();
		CrewTotalRecordDto myTotalRecord = userService.getMyTotalRecord(userDto.getUserSeq());
		return new ResponseEntity<>(new ResponseFrame<>(true, myTotalRecord, 1, "자신 누적 기록 조회에 성공했습니다."), HttpStatus.OK);
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
	@ApiOperation(value = "자신의 전체 기록 목록 조회 (year, month는 미입력 또는 0 입력 시 전체 조회)")
	@GetMapping("/activity")
	public ResponseEntity<?> getMyTotalRecordList(RecordParamsDto params) {
		/**
		 * int값은 안 들어올 때 0으로 들어오는것으로 알고, size, offset, year, month는 검색 조건이 있다면 0이 아닌
		 * 값이므로 0이 들어왔을 떼 예외적인 처리를 해야함.
		 */
		
		UserDto userDto = loadUserFromToken();
		params.setUserSeq(userDto.getUserSeq());
		params.setCrewSeq(null);
		
		HttpStatus httpStatus = HttpStatus.OK;
		
		ResponseFrame<List<RunRecordDto>> responseFrame = new ResponseFrame<>();
		List<RunRecordDto> myRecordList = null;
		try {
			
			myRecordList = runService.getMyRecordList(params);
			
		}catch (Exception e) {
			httpStatus = HttpStatus.CONFLICT;
			responseFrame.setCount(0);
			responseFrame.setSuccess(false);
			responseFrame.setMsg(e.getMessage());
		}
		
		if (myRecordList != null) {
			responseFrame.setCount(myRecordList.size());
			responseFrame.setSuccess(true);
			responseFrame.setMsg("자신의 전체 기록 목록 조회에 성공했습니다.");
		}
		responseFrame.setData(myRecordList);
		return new ResponseEntity<>(responseFrame, httpStatus);
	}

	/**
	 * 
	 * 2022-07-26 페이징 수정 손광진
	 * @throws Exception 
	 */
	@GetMapping("/boards") //안쓴 거는 null로 받기위해 참조형으로 파라메터를 받음
	public ResponseEntity<?> getMyBoards(@RequestParam(required = false,name = "size") Long size,
			@RequestParam(required = false,name = "boardMaxSeq") Long boardMaxSeq) throws Exception {
		
		UserDto userDto = loadUserFromToken();
		
		List<CrewBoardRes> myBoardList =  userService.getMyBoards(userDto.getUserSeq(),size,boardMaxSeq);
		
		ResponseFrame<?> res = ResponseFrame.of(myBoardList,myBoardList.size(),"자신이 쓴 글을 반환합니다.");
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	@ApiOperation(value = "자신이 오늘 크루에서 뛴 기록이 있는지 조회 (연습크루는 나중에 생각합시다.)")
	@GetMapping("/runabletoday/{crewSeq}")
	public ResponseEntity<?> getRunabletoday(@PathVariable Long crewSeq) throws Exception {
		// crewSeq가 연습크루일 경우 : true 반환
		
		UserDto userDto = loadUserFromToken();
		
		HttpStatus httpStatus = HttpStatus.OK;
		
		ResponseFrame<Boolean> responseFrame = new ResponseFrame<>();
		Boolean todayRecord = runService.getRunabletoday(userDto.getUserSeq(), crewSeq);
		return new ResponseEntity<>(new ResponseFrame<Boolean>(true, todayRecord, 1, "뛸 수 있는 여부 반환 완료."), HttpStatus.OK);
	}

	@ApiOperation("크루 내 달성한 기록들의 갯수를 가져오기")
	@GetMapping("/crew/{crewSeq}/succes-record/count")
	public ResponseEntity<?> getSuccesRecordCount(@PathVariable Long crewSeq) {
		UserDto userDto = loadUserFromToken();
		
		
		//userService.getCountSuccesRecordsInCrew(userDto.getUserSeq(),crewSeq);
		
		return null;
	}
	
}
