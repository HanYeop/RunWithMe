package com.ssafy.gumid101.crew;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.dto.CrewBoardDto;
import com.ssafy.gumid101.dto.RankingParamsDto;
import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.RunRecordDto;
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

	@GetMapping("/{crewSeq}/my")
	public RequestEntity<?> getCrewMyRecords(@ModelAttribute RecordParamsDto recordParamsDto){
		return null;
	}
	
	@GetMapping("/{crewSeq}/my-total")
	public RequestEntity<?> getCrewMyTotalRecords(@PathVariable long crewSeq){
		return null;
	}
	
	@PostMapping("/{crewSeq}/board")
	public RequestEntity<?> writeCrewBoards(@PathVariable long crewSeq, @RequestPart(name = "img", required = false) MultipartFile image, @RequestPart CrewBoardDto crewBoardDto){
		
		return null;
	}
	
	@GetMapping("/{crewSeq}/boards")
	public RequestEntity<?> getCrewBoards(@PathVariable long crewSeq){
		return null;
	}
	
	@DeleteMapping("/{crewSeq}/boards/{boardSeq}")
	public RequestEntity<?> deleteCrewBoards(@PathVariable long crewSeq, @PathVariable long boardSeq){
		return null;
	}
	
	@PostMapping("/{crewSeq}/records")
	public RequestEntity<?> writeRunRecord(@PathVariable long crewSeq, @RequestPart(name = "img") MultipartFile image, @RequestPart RunRecordDto runRecordDto){
		
		return null;
	}
}
