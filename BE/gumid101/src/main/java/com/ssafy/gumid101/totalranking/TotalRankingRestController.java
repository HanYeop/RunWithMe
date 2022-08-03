package com.ssafy.gumid101.totalranking;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.res.RankingDto;
import com.ssafy.gumid101.res.ResponseFrame;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import springfox.documentation.spi.service.contexts.SecurityContext;

@Api(tags = "전체 랭킹")
@RequestMapping("/total-ranking")
@RestController
@RequiredArgsConstructor
public class TotalRankingRestController {

	private final TotalRankingService totalRankingService;
	/**
	 * type = (dinstance,time,point)
	 * @param size
	 * @param offest
	 * @return
	 * @throws Exception 
	 */
	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}
	
	@ApiOperation("전체 랭킹 가져오기,입력 값 타입 {distance,time,point}")
	@RequestMapping(path = "/{type}",method = RequestMethod.GET)
	public ResponseEntity<?> getTotalRanking(@ApiParam("distance,time,point") @PathVariable(value = "type" ) String rankingType,@RequestParam Long size,@RequestParam Long offset) throws Exception{
		
		List<RankingDto> rankingList =  totalRankingService.getRankingByType(rankingType,size,offset);
		
		if(rankingList == null) {
			rankingList = new ArrayList<RankingDto>();
		}
		
		ResponseFrame<?> res =  ResponseFrame.of(rankingList, rankingList.size(), String.format("%s 에 따른 랭킹 리스트를 반환합니다.", rankingType));
		
		return new ResponseEntity<>(res,HttpStatus.OK);
		
	}
	
	@ApiOperation("내 랭킹 가져오기,입력 값 타입 {distance,time,point}")
	@RequestMapping(path = "my/{type}",method = RequestMethod.GET)
	public ResponseEntity<?> getTotalRanking(@ApiParam("distance,time,point") @PathVariable(value = "type" ) String rankingType) throws Exception{
		
		Long userSeq = loadUserFromToken().getUserSeq();
		
		RankingDto rankingList =  totalRankingService.getMyRankingByType(rankingType,userSeq);
		

		ResponseFrame<?> res =  ResponseFrame.of(rankingList, 1, String.format("%s 에 따른 랭킹 리스트를 반환합니다.", rankingType));
		
		return new ResponseEntity<>(res,HttpStatus.OK);
		
	}
}
