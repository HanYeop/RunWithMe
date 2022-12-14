package com.ssafy.gumid101.crew.activity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.dto.CrewBoardDto;
import com.ssafy.gumid101.dto.CrewTotalRecordDto;
import com.ssafy.gumid101.dto.RankingParamsDto;
import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.redis.RedisService;
import com.ssafy.gumid101.res.CrewBoardFileDto;
import com.ssafy.gumid101.res.GraphRecordDto;
import com.ssafy.gumid101.res.RankingDto;
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
	private final RedisService redisServ;

	public RequestEntity<?> getCrewRecord() {
		return null;
	}

	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}

	@ApiOperation(value = "검색 조건에 따라 기록의 목록을 조회한다. (URL의 crewSeq만 적용)")
	@GetMapping("/{crewSeq_p}/records")
	public ResponseEntity<?> getCrewRecordList(@PathVariable(name = "crewSeq_p") Long crewSeq,
			@ModelAttribute RecordParamsDto recordParamsDto) {
//		log.debug("크루 생성 시도 : 이름 {}, 설명 {}, ", crewDto.getCrewName());
		recordParamsDto.setCrewSeq(crewSeq);
		ResponseFrame<List<RunRecordDto>> responseMap = new ResponseFrame<>();
		HttpStatus httpStatus = HttpStatus.OK;

		List<RunRecordDto> crewRecordList = crewActivityService.getCrewRecordList(recordParamsDto);

		if (crewRecordList == null) {
			httpStatus = HttpStatus.OK;
			responseMap.setCount(0);
			responseMap.setSuccess(false);
		} else {
			responseMap.setCount(crewRecordList.size());
			responseMap.setSuccess(true);
		}
		responseMap.setData(crewRecordList);

		return new ResponseEntity<>(responseMap, httpStatus);
	}

	@ApiOperation("크루내 랭킹(미구현)")
	@GetMapping("/{crewSeq_p}/ranking")
	public ResponseEntity<?> getCrewRankings(@PathVariable(name = "crewSeq_p") Long crewSeq, @ModelAttribute RankingParamsDto rankingParamsDto) throws Exception {
		rankingParamsDto.setCrewSeq(crewSeq);
		List<RankingDto> rankingList = crewActivityService.getCrewRankingByParam(rankingParamsDto);

		if (rankingList == null) {
			rankingList = new ArrayList<RankingDto>();
		}

		ResponseFrame<List<RankingDto>> res = ResponseFrame.of(rankingList, rankingList.size(), "크루 내 랭킹을 반환합니다.");

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	/**
	 * 
	 * @param recordParamsDto
	 * @return
	 */
	@ApiOperation("크루내 내 기록 보기,리스트(미구현)")
	@GetMapping("/{crewSeq}/my")
	public ResponseEntity<?> getCrewMyRecords(@ModelAttribute RecordParamsDto recordParamsDto) throws Exception {
		recordParamsDto.setUserSeq(loadUserFromToken().getUserSeq());
		List<RunRecordDto> runRecordList = crewActivityService.getMyCrewRecordsByParam(recordParamsDto);

		if (runRecordList == null) {
			runRecordList = new ArrayList<RunRecordDto>();
		}

		ResponseFrame<List<RunRecordDto>> res = ResponseFrame.of(runRecordList, runRecordList.size(),
				"크루 내 누적기록을 반환합니다.");

		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@ApiOperation("크루내 내 그래프 데이터 받아오기(주의 : 크루의 자기 기록이 하루에 1개 또는 0개 있어야합니다. 같은 날 2개 이상 있으면 오류납니다.")
	@GetMapping("/{crewSeq}/mygraphdata/{goalType}")
	public ResponseEntity<?> getCrewMyGraphData(@PathVariable Long crewSeq, @PathVariable String goalType) throws Exception {
		UserDto userDto = loadUserFromToken();
		
		List<GraphRecordDto> datas = crewActivityService.getCrewMyGraphData(userDto, crewSeq, goalType);
		
		return new ResponseEntity<>(ResponseFrame.of(datas, datas.size(), "크루 내 내 그래프 데이터를 받아옵니다."), HttpStatus.OK);
	}
	

	@ApiOperation("크루내 내 통합 누적 기록 보기")
	@GetMapping("/{crewSeq}/my-total")
	public ResponseEntity<?> getCrewMyTotalRecords(@PathVariable Long crewSeq) throws Exception {

		CrewTotalRecordDto myTotalRecord = crewActivityService.getMyCrewTotalRecord(loadUserFromToken().getUserSeq(), crewSeq);

		if (myTotalRecord == null) {
			myTotalRecord = CrewTotalRecordDto.defaultCrewTotalRecordDto();
		}

		ResponseFrame<CrewTotalRecordDto> res = ResponseFrame.of(myTotalRecord, 1, "크루 내 나의 누적 기록을 반환합니다.");

		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	@PostMapping(value = "/{crewSeq}/board",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	@ApiOperation("크루 게시판 글 작성")
	public ResponseEntity<?> writeCrewBoards(
			@PathVariable(name = "crewSeq") Long crewSeq,
			@RequestPart(name= "crewBoardDto", required = true) CrewBoardDto crewBoardDto,
			@RequestPart(name = "imgFile", required = false) MultipartFile imageFile) throws Exception {
		
		
		UserDto writerUser = loadUserFromToken();
		redisServ.getIsUseable(writerUser.getUserSeq().toString() + "writeCrewBoards", 5);

		CrewBoardFileDto crewBoardFileDto = crewActivityBoardService.writeBoard(writerUser, imageFile, crewBoardDto, crewSeq);
		return new ResponseEntity<>(new ResponseFrame<>(true, crewBoardFileDto, 1, "글 작성에 성공했습니다."), HttpStatus.OK);
	}

	@ApiOperation(value = "크루 게시판 글 조회. (size : 전체조회 -1, 미입력시 10 / offset : 미입력시 0/maxCrewBoardSeq : 최고 id or null")
	@GetMapping("/{crewSeq}/boards")
	public ResponseEntity<?> getCrewBoards(@PathVariable Long crewSeq, @RequestParam(required = false) Integer size,
			@RequestParam(required = false) Long maxCrewBoardSeq) throws Exception {

		List<CrewBoardFileDto> crewBoardFileDtoList = crewActivityBoardService.getCrewBoards(crewSeq, size, maxCrewBoardSeq);
		return new ResponseEntity<>(new ResponseFrame<>(true, crewBoardFileDtoList, crewBoardFileDtoList.size(), "글 조회 성공"), HttpStatus.OK);
	}

	@ApiOperation("크루 내 게시글 삭제")
	@DeleteMapping("/crew/boards/{boardSeq}")
	public ResponseEntity<?> deleteCrewBoards(@PathVariable Long boardSeq) throws Exception {
		
		Boolean deleteSuccess  = crewActivityBoardService.deleteCrewBoard(boardSeq);
		return new ResponseEntity<>(new ResponseFrame<>(true, deleteSuccess, 1, "글 삭제 성공"), HttpStatus.OK);
	}
	
	


}
