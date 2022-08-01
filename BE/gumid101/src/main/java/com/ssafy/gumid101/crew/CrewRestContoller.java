package com.ssafy.gumid101.crew;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.req.PasswordDto;
import com.ssafy.gumid101.res.CrewUserDto;
import com.ssafy.gumid101.res.ResponseFrame;
import com.ssafy.gumid101.res.RunRecordResultDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "크루 컨트롤러")
@RequiredArgsConstructor
@RestController
@RequestMapping("/crew")
public class CrewRestContoller {

	private final CrewService crewService;
	private final ObjectMapper objectMapper;

	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}

	@ApiOperation("런 레코드 등록(구현중)")
	@PostMapping(value= "/{crewId}/records",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> recordMyRun(
			@PathVariable("crewId") Long crewId ,
			@RequestPart(value="runRecord",required = true) String runRecord,
			@RequestPart MultipartFile imgFile) throws Exception{
		UserDto userDto =  loadUserFromToken();
		
		Long userSeq = userDto.getUserSeq();
		
		RunRecordDto runRecordDto = objectMapper.readValue(runRecord, RunRecordDto.class);
		
		RunRecordResultDto runRecordResult = crewService.insertUserRunRecordAsCrew(userSeq,crewId,runRecordDto,imgFile);
		
		ResponseFrame<RunRecordResultDto> res= ResponseFrame.of(runRecordResult, 0, "러닝 기록 완료 결과에 대해 반환합니다.");
		
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	@ApiOperation(value = "크루가입")
	@PostMapping(value="/{crewId}/join")
	public ResponseEntity<?> jonCrew(@PathVariable(required = true) long crewId, @RequestBody(required = false) Optional<PasswordDto>  password)
			throws Exception {

		UserDto userDto = loadUserFromToken();

		
		CrewUserDto result = crewService.joinCrew(userDto.getUserSeq(), crewId, password);

		ResponseFrame<CrewUserDto> res = ResponseFrame.of(result, 1, "사용자의 크루 가입 성공");

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

}
