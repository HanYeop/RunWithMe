package com.ssafy.gumid101.crew;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.dto.UserDto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/crew")
public class CrewRestContoller {
	
	private final CrewService crewService;
	
	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}
	
	public ResponseEntity<?> recordMyRun(){
		UserDto userDto =  loadUserFromToken();
		
		//return new ResponseEntity<T>();
		return null;
	}
	
	@PostMapping("")
	public  ResponseEntity<?>  jonCrew(@PathVariable(required = true) long crewId,@RequestBody String passwrod ) {
		
		UserDto userDto =  loadUserFromToken();
		
		//int result = crewService.joinCrew(userDto.getUserSeq(),crewId,passwrod);
		
		
		//return new ResponseEntity<T>();
		return null;
	}
}
