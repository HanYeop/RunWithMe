package com.ssafy.gumid101.crew;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.customexception.CrewNotFoundException;
import com.ssafy.gumid101.customexception.PasswrodNotMatchException;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.res.CrewUserDto;
import com.ssafy.gumid101.res.ResponseFrame;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@Api(tags = "크루 컨트롤러")
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
	
	@ApiOperation("런 레코드 등록(미구현)")
	@PostMapping("/crew/{crewId}/records")
	public ResponseEntity<?> recordMyRun(@PathVariable("crewId") Long crewId ,@ModelAttribute RunRecordDto runRecord,@RequestPart MultipartFile imgFile){
		UserDto userDto =  loadUserFromToken();
		
		//return new ResponseEntity<T>();
		return null;
	}
	@ApiOperation(value = "크루가입")
	@PostMapping("/{crewId}/join")
	public  ResponseEntity<?>  jonCrew(@PathVariable(required = true) long crewId,@RequestBody String passwrod ) throws Exception {
		
		UserDto userDto =  loadUserFromToken();
		
		CrewUserDto result = crewService.joinCrew(userDto.getUserSeq(),crewId,passwrod);
		
		ResponseFrame<CrewUserDto> res = ResponseFrame.of(result, 1, "사용자의 크루 가입 성공");
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<?> userSeqNotFoundHandler(UsernameNotFoundException e){ 
		return new ResponseEntity<>(ResponseFrame.of(false, e.getMessage()),HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(CrewNotFoundException.class)
	public ResponseEntity<?> crewSeqNotFoundHandler(UsernameNotFoundException e){ 
		
		return new ResponseEntity<>(ResponseFrame.of(false, e.getMessage()),HttpStatus.FORBIDDEN);
	
	}
	
	@ExceptionHandler(PasswrodNotMatchException.class)
	public ResponseEntity<?> crewSeqNotFoundHandler(PasswrodNotMatchException e){ 
		
		return new ResponseEntity<>(ResponseFrame.of(false, e.getMessage()),HttpStatus.FORBIDDEN);
	
	}
}
