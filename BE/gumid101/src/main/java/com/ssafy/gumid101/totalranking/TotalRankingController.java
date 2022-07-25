package com.ssafy.gumid101.totalranking;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.res.RankingDto;
import com.ssafy.gumid101.res.ResponseFrame;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "전체 랭킹")
@RequestMapping("/total-ranking")
@RestController
public class TotalRankingController {

	TotalRankingService totalRankingService;
	/**
	 * type = (dinstance,time,point)
	 * @param size
	 * @param offest
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation("전체 랭킹 가져오기")
	@RequestMapping("/{type}")
	public ResponseEntity<?> getTotalRanking(@ApiParam("distance,time,point") @PathVariable String rankingType,@RequestParam Long size,@RequestParam Long offset) throws Exception{
		
		List<RankingDto> rankingList =  totalRankingService.getRankingByType(rankingType,size,offset);
		if(rankingList == null) {
			rankingList = new ArrayList<RankingDto>();
		}
		
		ResponseFrame<?> res =  ResponseFrame.of(rankingList, rankingList.size(), String.format("$s 에 따른 랭킹 리스트를 반환합니다.", rankingType));
		
		return new ResponseEntity<>(res,HttpStatus.OK);
		
	}
	
}
