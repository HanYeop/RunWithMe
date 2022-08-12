package com.ssafy.gumid101.competition;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.gumid101.dto.CompetitionDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.redis.RedisService;
import com.ssafy.gumid101.res.CompetitionFileDto;
import com.ssafy.gumid101.res.RankingDto;
import com.ssafy.gumid101.res.ResponseFrame;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/competition")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "시즌제 대회 컨트롤러")
public class CompetitionRestController {
	
	private final RedisService redisServ;
	private final CompetitionService compServ;
	private final ObjectMapper objectMapper;

	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}

	@ApiOperation(value = "대회 만들기")
	@PostMapping(value = "/")
	public ResponseEntity<?> makeCompetition(@RequestPart(name = "competitionDto") String competitionDtoString,
			@RequestPart(name = "competitionImageFile", required = false) MultipartFile competitionImageFile) throws Exception{
		CompetitionDto competitionDto = objectMapper.readValue(competitionDtoString, CompetitionDto.class);
		redisServ.getIsUseable(competitionDto.getCompetitionName() + "makeCompetition", 3);
		return new ResponseEntity<>(new ResponseFrame<>(true, compServ.makeCompetition(competitionDto, competitionImageFile), 1, "대회 개최에 성공했습니다."), HttpStatus.OK);
	}
	
	@ApiOperation(value = "대회 조회 API - 시작 전")
	@GetMapping(value = "/beforestart")
	public ResponseEntity<?> getCompetitionBeforeStart() throws Exception{
		List<CompetitionFileDto> competitionFileDtolist = compServ.getCompetitionBeforeStart();
		return new ResponseEntity<>(new ResponseFrame<>(true, competitionFileDtolist, competitionFileDtolist.size(), "대회 조회에 성공했습니다."), HttpStatus.OK);
	}
	
	@ApiOperation(value = "대회 조회 API - 진행 중")
	@GetMapping(value = "/inprogress")
	public ResponseEntity<?> getCompetitionProgress() throws Exception{
		CompetitionFileDto competitionFileDto = compServ.getCompetitionProgress();
		return new ResponseEntity<>(new ResponseFrame<>(true, competitionFileDto, 1, "대회 조회에 성공했습니다."), HttpStatus.OK);
	}

	@ApiOperation(value = "대회 조회 API - 진행 중, 해당 유저 참여")
	@GetMapping(value = "/inprogress/{userSeq}")
	public ResponseEntity<?> getCompetitionProgress(@PathVariable Long userSeq) throws Exception{
		CompetitionFileDto competitionFileDto = compServ.getCompetitionProgress(userSeq);
		return new ResponseEntity<>(new ResponseFrame<>(true, competitionFileDto, 1, "대회 조회에 성공했습니다."), HttpStatus.OK);
	}
	
	@ApiOperation(value = "대회 조회 API - 종료")
	@GetMapping(value = "/afterend")
	public ResponseEntity<?> getCompetitionAfterEnd() throws Exception{
		List<CompetitionFileDto> competitionFileDtolist = compServ.getCompetitionAfterEnd();
		return new ResponseEntity<>(new ResponseFrame<>(true, competitionFileDtolist, competitionFileDtolist.size(), "대회 조회에 성공했습니다."), HttpStatus.OK);
	}
	
	@ApiOperation(value = "대회 조회 API - 종료, 해당 유저 참여")
	@GetMapping(value = "/afterend/{userSeq}")
	public ResponseEntity<?> getCompetitionAfterEnd(@PathVariable Long userSeq) throws Exception{
		List<CompetitionFileDto> competitionFileDtolist = compServ.getCompetitionAfterEnd(userSeq);
		return new ResponseEntity<>(new ResponseFrame<>(true, competitionFileDtolist, competitionFileDtolist.size(), "대회 조회에 성공했습니다."), HttpStatus.OK);
	}
	
	@ApiOperation(value = "대회 참여 가능 여부 조회")
	@GetMapping(value = "/{competitionSeq}/check/{userSeq}")
	public ResponseEntity<?> checkCompetitionJoinable(@PathVariable Long competitionSeq, @PathVariable Long userSeq) throws Exception{
		Boolean joinable = compServ.checkCompetitionJoinable(competitionSeq, userSeq);
		return new ResponseEntity<>(new ResponseFrame<>(true, joinable, joinable ? 1 : 0, "해당 유저는 해당 대회에 참여" + (joinable ? "" : "불") + "가능합니다."), HttpStatus.OK);
	}
	
	@ApiOperation(value = "대회 참여자 수 조회")
	@GetMapping(value = "/{competitionSeq}/participant")
	public ResponseEntity<?> countParticipantCompetition(@PathVariable Long competitionSeq) throws Exception{
		Long count = compServ.countParticipantCompetition(competitionSeq);
		return new ResponseEntity<>(new ResponseFrame<>(true, count, count.intValue(), "해당 대회의 참가자 수는 " + count + "명 입니다."), HttpStatus.OK);
	}
	
	@ApiOperation(value = "대회 참가")
	@PostMapping(value = "/{competitionSeq}/join")
	public ResponseEntity<?> checkCompetitionJoinable(@PathVariable Long competitionSeq) throws Exception{
		UserDto userDto = loadUserFromToken();
		Boolean result = compServ.joinCompetition(competitionSeq, userDto.getUserSeq());
		return new ResponseEntity<>(new ResponseFrame<>(true, result, result ? 1 : 0, "대회 참가에 " + (result ? "성공" : "실패") + "했습니다."), HttpStatus.OK);
	}
	
	@ApiOperation("대회 전체 랭킹 가져오기")
	@GetMapping(value = "/{competitionSeq}/ranking")
	public ResponseEntity<?> getCompetitionTotalRanking(@PathVariable Long competitionSeq, @RequestParam(required = false, defaultValue = "10") Long size, @RequestParam(required = false, defaultValue = "0") Long offset) throws Exception{
		
		List<RankingDto> rankingList = compServ.getCompetitionTotalRanking(competitionSeq, size, offset);
		
		if(rankingList == null) {
			rankingList = new ArrayList<RankingDto>();
		}
		
		ResponseFrame<?> res =  ResponseFrame.of(rankingList, rankingList.size(), "대회 랭킹 리스트를 반환합니다.");
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
	
	@ApiOperation("대회 유저 랭킹 가져오기")
	@GetMapping(value = "/{competitionSeq}/ranking/{userSeq}")
	public ResponseEntity<?> getCompetitionUserRanking(@PathVariable Long competitionSeq, @PathVariable Long userSeq) throws Exception{
		
		RankingDto rankingInfo = compServ.getCompetitionUserRanking(competitionSeq, userSeq);
		
		ResponseFrame<?> res = ResponseFrame.of(rankingInfo, 1, "대회 랭킹 리스트를 반환합니다.");
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}	
}
