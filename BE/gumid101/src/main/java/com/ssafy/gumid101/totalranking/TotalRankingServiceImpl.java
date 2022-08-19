package com.ssafy.gumid101.totalranking;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.gumid101.res.RankingDto;
import com.ssafy.gumid101.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TotalRankingServiceImpl implements TotalRankingService {

	private final CrewTotalRecordRepository totalRankingRepo;
	private final UserRepository userRepo;

	/**
	 * 랭킹 뿌려줌
	 */
	@Override
	public List<RankingDto> getRankingByType(String rankingType, Long size, Long offset) throws Exception {
		List<RankingDto> rankingList = null;
		//totalRankingRepo
		if(!"point".equals(rankingType)) {
			rankingList= totalRankingRepo.getUserTotalRankingOptionType(rankingType, size, offset);	
		}else if("point".equals(rankingType)) {
			rankingList= userRepo.getUserTotalPointRanking( size,  offset);
		}
		
		return rankingList;
	}
//크루이름 유저이름 , 유저 SEQ, 크루 SEQ
	/**
	 * 자기 랭킹 뿌려줌
	 */
	@Override
	public RankingDto getMyRankingByType(String rankingType, Long userSeq) throws Exception {

		RankingDto ranking =  totalRankingRepo.getMyTotalRanking(rankingType,userSeq);
	
		return ranking;
	}


}
