package com.ssafy.gumid101.crew.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.gumid101.customexception.CrewPermissonDeniedException;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.RecruitmentParamsDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.jwt.JwtUtilsService;
import com.ssafy.gumid101.res.CrewFileDto;
import com.ssafy.gumid101.res.ResponseFrame;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/crew-manager")
@RequiredArgsConstructor
@Api(tags = "크루 관리 컨트롤러")
@Slf4j
public class CrewManagerRestController {

	private final JwtUtilsService jwtUtilService;
	private final CrewManagerService crewManagerService;
	private final ObjectMapper objectMapper;
	
	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}
	
	

	@ApiOperation(value="자신이 해당 크루 소속인지 조회")
	@GetMapping("/{crewSeq}/membercheck")
	public ResponseEntity<?> crewMemberCheck(@PathVariable Long crewSeq) throws Exception{
		
		UserDto userDto= loadUserFromToken();
		
		Boolean check =  crewManagerService.isUserCrewMember(userDto.getUserSeq(), crewSeq);
		
		ResponseFrame<?> res = ResponseFrame.of(check, 0, "자신이 해당 크루원인지 여부가 조회되었습니다.");
		
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	
	
	@ApiOperation(value="진행 중인 내 크루보기 정렬 아직 없음")
	@GetMapping("/my-current-crew")
	public ResponseEntity<?> getMyCurrentCrew() throws Exception{
		
		UserDto userDto= loadUserFromToken();
		
		List<?> crewList =  crewManagerService.getMyCurrentCrew(userDto.getUserSeq());
		
		ResponseFrame<?> res = ResponseFrame.of(crewList, crewList.size(), "현재 진행중, 진행 예정인 나의 현재 크루가 반환되었습니다.");
		
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	
	@ApiOperation("내 끝난 크루를 조회 why? 업적 조회할 떄,")
	@GetMapping("/my-end-crew")
	public RequestEntity<?> getMyEndCrew(){
		return null;
	}
	
	/**
	 * 
	 * @param paramsDto
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation("모집 중인 크루 리스트 보기")
	@GetMapping("/recruitment")
	public ResponseEntity<?> getCrewRecruitment(@ModelAttribute RecruitmentParamsDto paramsDto) throws Exception{
		

		
		List<CrewFileDto> crewList =  crewManagerService.crewSearcheByRecruitmentParams(paramsDto);
		if(crewList == null) {
			crewList = new ArrayList<CrewFileDto>();
		}
		ResponseFrame<?> res = ResponseFrame.of(crewList, crewList.size(), "모집중인 크루 리스트를 반환합니다.");
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	/**
	 * 크루 생성하기
	 * @param crewDto
	 * @return
	 */
	@ApiOperation(value = "크루 생성하기(모집하기)")
	@PostMapping("/crew")
	public ResponseEntity<?> createCrew(@RequestPart("crewDto") String crewDto, @RequestPart(name = "img", required = false) MultipartFile image) throws Exception{

		UserDto managerDto = loadUserFromToken();
		HttpStatus httpStatus = HttpStatus.OK;
		ResponseFrame<CrewFileDto> responseMap = new ResponseFrame<>();
		
		CrewDto crewteCrewDto = objectMapper.readValue(crewDto, CrewDto.class);
		crewteCrewDto.setCrewMemberCount(1);
		CrewFileDto crewFileDto = null;
		try {
			crewFileDto = crewManagerService.createCrew(image, crewteCrewDto, managerDto);
		}catch (Exception e) {
			httpStatus = HttpStatus.OK;
			responseMap.setCount(0);
			responseMap.setData(new CrewFileDto(null, null));
			responseMap.setSuccess(false);
			responseMap.setMsg(e.getMessage());
		}
		
		if (crewFileDto != null) {
			responseMap.setCount(1);
			responseMap.setSuccess(true);
			responseMap.setMsg("크루 생성에 성공했습니다.");
		}
		responseMap.setData(crewFileDto);
		return new ResponseEntity<>(responseMap, httpStatus);
	}
	
	/**
	 * 크루 삭제하기 , 크루장만 가능 , 조건 - 시작일 전만
	 * @param crewSeq
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "크루장이 크루 해체")
	@DeleteMapping("/crew/{crewSeq}")
	public ResponseEntity<?> deleteCrew(@PathVariable(required = true) long crewSeq) throws Exception{
		
		UserDto userDto = loadUserFromToken();
		
		int result = crewManagerService.deleteCrew(crewSeq,userDto.getUserSeq());
		
		ResponseFrame<Integer> res = ResponseFrame.of(result,result,"크루장의 크루삭제가 완료되었습니다.");
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("/crew/{crewSeq}")
	public ResponseEntity<?> getCrewDetail(@PathVariable Long crewSeq) throws Exception{
		
		CrewFileDto  crewFileDto = crewManagerService.getCrewDetail(crewSeq);
		
		ResponseFrame<CrewFileDto> resFrame = ResponseFrame.of(crewFileDto, 1, "크루 상세정보를 반환합니다.");
				
		
		return new ResponseEntity<>(resFrame,HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param crewSeq
	 * @param password
	 * @return
	 */
	@PostMapping("/crew/{crewSeq}/user")
	public RequestEntity<?> joinCrew(@PathVariable long crewSeq, @RequestParam String password){
		return null;//이거 crewController에 있는 듯?
	}
	
	/**
	 * 크루원이 크루 시작전 크루 탈퇴
	 * @param crewSeq
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation("크루 탈퇴")
	@DeleteMapping("/crew/{crewSeq}/user")
	public ResponseEntity<?> exitCrew(@PathVariable long crewSeq) throws Exception{
		UserDto user =  loadUserFromToken();
		
		int result = crewManagerService.exitCrew(crewSeq,user.getUserSeq());
		
		ResponseFrame<?> frame;
		if(1==result) {
			frame = ResponseFrame.of(result, result, "정상적으로 탈퇴되었습니다.");
		}else {
			frame = ResponseFrame.of(false, "탈퇴 중에 오류 발생");
		}
		
		return new ResponseEntity<>(frame,HttpStatus.OK);
	}
	
	
	@ExceptionHandler(NotFoundUserException.class)
	public ResponseEntity<?> userNofoundControll(NotFoundUserException nue){
		
		return new ResponseEntity<>(ResponseFrame.of(false, nue.getMessage()),HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(CrewPermissonDeniedException.class)
	public ResponseEntity<?> crewPermisionDnieHandler(NotFoundUserException nue){
		
		return new ResponseEntity<>(ResponseFrame.of(false, nue.getMessage()),HttpStatus.FORBIDDEN);
	}
}
