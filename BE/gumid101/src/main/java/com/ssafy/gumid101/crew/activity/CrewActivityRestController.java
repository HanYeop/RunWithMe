package com.ssafy.gumid101.crew.activity;

import java.util.List;

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

import com.ssafy.gumid101.dto.CrewBoardDto;
import com.ssafy.gumid101.dto.RankingParamsDto;
import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.res.CrewBoardFileDto;
import com.ssafy.gumid101.res.ResponseFrame;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/crew-activity")
@RequiredArgsConstructor
@Api(tags = "크루 글, 기록, 달리기 등 크루 활동 API")
public class CrewActivityRestController {
	
	private final CrewActivityService crewActivityService;
	private final CrewActivityBoardService crewActivityBoardService;

	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}
	
	public RequestEntity<?> getCrewRecord(){
		return null;
	}
	
	@ApiOperation(value = "검색 조건에 따라 기록의 목록을 조회한다. (URL의 crewSeq만 적용)")
	@GetMapping("/{crewSeq_p}/records")
	public ResponseEntity<?> getCrewRecordList(@PathVariable(name = "crewSeq_p") String crewSeq, @ModelAttribute RecordParamsDto recordParamsDto){
//		log.debug("크루 생성 시도 : 이름 {}, 설명 {}, ", crewDto.getCrewName());
		recordParamsDto.setCrewSeq(Long.parseLong(crewSeq));
		ResponseFrame<List<RunRecordDto>> responseMap = new ResponseFrame<>();
		HttpStatus httpStatus = HttpStatus.OK;
		
		List<RunRecordDto> crewRecordList = crewActivityService.getCrewRecordList(recordParamsDto);

		if (crewRecordList == null) {
			httpStatus = HttpStatus.CONFLICT;
			responseMap.setCount(0);
			responseMap.setSuccess(false);
		} else {
			responseMap.setCount(crewRecordList.size());
			responseMap.setSuccess(true);
		}
		responseMap.setData(crewRecordList);

		return new ResponseEntity<>(responseMap, httpStatus);
	}
	
	@GetMapping("/{crewSeq}/ranking")
	public RequestEntity<?> getCrewRankings(@ModelAttribute RankingParamsDto rankingParamsDto){
		return null;
	}

	@ApiOperation(value = "크루 내 나의 기록 조회")
	@GetMapping("/{crewSeq_p}/my")
	public ResponseEntity<?> getCrewMyRecords(@PathVariable(name = "crewSeq_p") String crewSeq, @ModelAttribute RecordParamsDto recordParamsDto){
		recordParamsDto.setCrewSeq(Long.parseLong(crewSeq));
		ResponseFrame<List<RunRecordDto>> responseMap = new ResponseFrame<>();
		HttpStatus httpStatus = HttpStatus.OK;
		
		List<RunRecordDto> crewRecordList = crewActivityService.getCrewRecordList(recordParamsDto);

		if (crewRecordList == null) {
			httpStatus = HttpStatus.CONFLICT;
			responseMap.setCount(0);
			responseMap.setSuccess(false);
		} else {
			responseMap.setCount(crewRecordList.size());
			responseMap.setSuccess(true);
		}
		responseMap.setData(crewRecordList);

		return new ResponseEntity<>(responseMap, httpStatus);
	}
	
	@GetMapping("/{crewSeq}/my-total")
	public RequestEntity<?> getCrewMyTotalRecords(@PathVariable long crewSeq){
		return null;
	}
	
	@PostMapping("/{crewSeq}/board")
	@ApiOperation(value = "크루 게시판 글 작성")
	public ResponseEntity<?> writeCrewBoards(@PathVariable Long crewSeq,
			@RequestPart(name = "img", required = false) MultipartFile image,
			@ModelAttribute CrewBoardDto crewBoardDto){
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto writerUser = (UserDto) autentication.getPrincipal();

		ResponseFrame<CrewBoardFileDto> responseFrame = new ResponseFrame<>();
		HttpStatus httpStatus = HttpStatus.OK; 
		
		CrewBoardFileDto crewBoardFileDto = null;
		try {
			crewBoardFileDto = crewActivityBoardService.writeBoard(writerUser, image, crewBoardDto, crewSeq);
		}catch (Exception e) {
			httpStatus = HttpStatus.CONFLICT;
			responseFrame.setCount(0);
			responseFrame.setSuccess(false);
			responseFrame.setMsg(e.getMessage());
		}
		
		if (crewBoardFileDto != null) {
			responseFrame.setCount(1);
			responseFrame.setSuccess(true);
			responseFrame.setMsg("글 작성에 성공했습니다.");
		}
		responseFrame.setData(crewBoardFileDto);
		return new ResponseEntity<>(responseFrame, httpStatus);
	}
	
	@ApiOperation(value = "크루 게시판 글 조회. (size : 전체조회 -1, 미입력시 10 / offset : 미입력시 0")
	@GetMapping("/{crewSeq}/boards")
	public ResponseEntity<?> getCrewBoards(@PathVariable Long crewSeq, @RequestParam(required = false) Integer size, @RequestParam(required = false) Long offset){
		
		ResponseFrame<List<CrewBoardFileDto>> responseFrame = new ResponseFrame<>();
		HttpStatus httpStatus = HttpStatus.OK; 
		
		List<CrewBoardFileDto> crewBoardFileDtoList = null;
		try {
			crewBoardFileDtoList = crewActivityBoardService.getCrewBoards(crewSeq, size, offset);
		}catch (Exception e) {
			httpStatus = HttpStatus.CONFLICT;
			responseFrame.setCount(0);
			responseFrame.setSuccess(false);
			responseFrame.setMsg(e.getMessage());
		}
		
		if (crewBoardFileDtoList != null) {
			responseFrame.setCount(crewBoardFileDtoList.size());
			responseFrame.setSuccess(true);
			responseFrame.setMsg("글 조회 성공");
		}
		responseFrame.setData(crewBoardFileDtoList);
		return new ResponseEntity<>(responseFrame, httpStatus);
	}
	
	@DeleteMapping("/{crewSeq}/boards/{boardSeq}")
	public ResponseEntity<?> deleteCrewBoards(@PathVariable Long crewSeq, @PathVariable Long boardSeq){
		Boolean deleteSuccess = null;
		HttpStatus httpStatus = HttpStatus.OK;
		ResponseFrame<Boolean> responseFrame = new ResponseFrame<>();
		try {
			deleteSuccess = crewActivityBoardService.deleteCrewBoard(crewSeq, boardSeq);
		}catch (Exception e) {
			httpStatus = HttpStatus.CONFLICT;
			responseFrame.setCount(0);
			responseFrame.setSuccess(false);
			responseFrame.setMsg(e.getMessage());
		}
		
		if (deleteSuccess != null) {
			responseFrame.setCount(1);
			responseFrame.setSuccess(true);
			responseFrame.setMsg("글 삭제 성공");
		}
		responseFrame.setData(deleteSuccess);
		return new ResponseEntity<>(responseFrame, httpStatus);
	}
	
	@PostMapping("/{crewSeq}/records")
	public RequestEntity<?> writeRunRecord(@PathVariable long crewSeq, @RequestPart(name = "img") MultipartFile image, @RequestPart RunRecordDto runRecordDto){
		
		return null;
	}
}
