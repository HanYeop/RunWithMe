package com.ssafy.gumid101.crew;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}
	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value="진행 중인 내 크루보기")
	@GetMapping("/my-current-crew")
	public RequestEntity<?> getMyCurrentCrew() throws Exception{
		
		UserDto userDto= loadUserFromToken();
		
		List<CrewDto> crewList =  crewManagerService.getMyCurrentCruew(userDto.getId());
		return null;
	}
	
	@GetMapping("/my-end-crew")
	public RequestEntity<?> getMyEndCrew(){
		return null;
	}
	
	@GetMapping("/recruitment")
	public RequestEntity<?> getCrewRecruitment(@ModelAttribute RecruitmentParamsDto paramsDto){
		
		return null;
	}
	
	/**
	 * 크루 생성하기
	 * @param crewDto
	 * @return
	 */
	@ApiOperation(value = "크루 생성하기(모집하기)")
	@PostMapping("/crew")
	public ResponseEntity<?> createCrew(@RequestPart(name = "img", required = false) MultipartFile image, @ModelAttribute CrewDto crewDto) throws Exception{
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto managerDto = (UserDto) autentication.getPrincipal();
		HttpStatus httpStatus = HttpStatus.OK;
		ResponseFrame<CrewFileDto> responseMap = new ResponseFrame<>();
		
		CrewFileDto crewFileDto = null;
		try {
			crewFileDto = crewManagerService.createCrew(image, crewDto, managerDto);
		}catch (Exception e) {
			httpStatus = HttpStatus.CONFLICT;
			responseMap.setCount(0);
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
	
	@DeleteMapping("/crew/{crewSeq}")
	public RequestEntity<?> deleteCrew(@PathVariable long crewSeq){
		return null;
	}
	
	@GetMapping("/crew/{crewSeq}")
	public RequestEntity<?> getCrewDetail(@PathVariable long crewSeq){
		return null;
	}
	
	@PostMapping("/crew/{crewSeq}/user")
	public RequestEntity<?> joinCrew(@PathVariable long crewSeq, @RequestParam String password){
		return null;
	}
	
	@DeleteMapping("/crew/{crewSeq}/user")
	public RequestEntity<?> exitCrew(@PathVariable long crewSeq){
		return null;
	}
}
