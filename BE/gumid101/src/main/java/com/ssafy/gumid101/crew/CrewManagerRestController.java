package com.ssafy.gumid101.crew;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.RecruitmentParamsDto;
import com.ssafy.gumid101.dto.UserDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "크루 매니저")
@RestController
@RequestMapping("/crew-manager")
@RequiredArgsConstructor
public class CrewManagerRestController {
	
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
	
	@PostMapping("/crew")
	public RequestEntity<?> createCrew(@ModelAttribute CrewDto crewDto){
		
		return null;
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
