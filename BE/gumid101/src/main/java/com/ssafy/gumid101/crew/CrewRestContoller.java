package com.ssafy.gumid101.crew;

import java.util.List;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.gumid101.dto.CoordinateDto;
import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.RecordCoordinateDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.redis.RedisService;
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
	private final RedisService redisServ;

	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}

	@ApiOperation("런 레코드 등록")
	@PostMapping(value= "/{crewSeq}/records")
	public ResponseEntity<?> recordMyRun(
			@PathVariable("crewSeq") Long crewSeq ,
			@RequestPart(value="runRecord",required = true) String runRecordString,
			@RequestPart MultipartFile imgFile) throws Exception{
		
		UserDto userDto =  loadUserFromToken();
		redisServ.getIsUseable(userDto.getUserSeq().toString() + "recordMyRun", 10);
		RunRecordDto runRecordDto = objectMapper.readValue(runRecordString, RunRecordDto.class);
		
		Long userSeq = userDto.getUserSeq();
		
		RunRecordResultDto runRecordResult = crewService.insertUserRunRecordAsCrew(userSeq, crewSeq, runRecordDto, imgFile);
		
		ResponseFrame<RunRecordResultDto> res= ResponseFrame.of(runRecordResult, 0, "러닝 기록 완료 결과에 대해 반환합니다.");
		
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}

	
	@ApiOperation(value = "크루가입")
	@PostMapping(value="/{crewSeq}/join")
	public ResponseEntity<?> joinCrew(@PathVariable(required = true) Long crewSeq, @RequestBody(required = false) Optional<PasswordDto> password)
			throws Exception {

		UserDto userDto = loadUserFromToken();
		redisServ.getIsUseable(userDto.getUserSeq().toString() + "joinCrew", 10);

		
		CrewUserDto result = crewService.joinCrew(userDto.getUserSeq(), crewSeq, password);

		ResponseFrame<CrewUserDto> res = ResponseFrame.of(result, 1, "사용자의 크루 가입 성공");

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	

	@PostMapping(value="/records/{recordseq}/coordinate")
	@ApiOperation("런레코드에 좌표값을 등록한다.")
	public ResponseEntity<?>  recordAddCoordinate(@PathVariable("recordseq") Long recordSeq,@RequestBody List<CoordinateDto> coordinates) throws Exception{
		
		int result = crewService.setRecordCoordinate(recordSeq,coordinates);
		
		boolean success =  result == 1 ? true: false;
		
		
		ResponseFrame<Integer> res = ResponseFrame.of(result, result, String.format("런레코드 등록이 %s하였습니다.", success ? "성공":"실패"));
		
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	@ApiOperation("런레코드의 좌표값을 가져온다.")
	@GetMapping(value = "/records/{recordseq}/coordinate")
	public ResponseEntity<?>  recordAddCoordinate(@PathVariable("recordseq") Long recordSeq) throws Exception{
		
		List<RecordCoordinateDto> coordinateDtoList  = crewService.getCoordinateByRunRecordSeq(recordSeq);

		ResponseFrame<List<RecordCoordinateDto>> res = ResponseFrame.of(coordinateDtoList, coordinateDtoList.size(), String.format("런레코드 SEQ : %d 에 해당하는 좌표들을 반환합니다.", recordSeq));
		
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
}
