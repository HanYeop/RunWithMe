package com.ssafy.gumid101.competition;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.dto.CompetitionDto;
import com.ssafy.gumid101.res.CompetitionFileDto;
import com.ssafy.gumid101.res.RankingDto;

public interface CompetitionService {

	CompetitionFileDto makeCompetition(CompetitionDto competitionDto, MultipartFile competitionImageFile) throws Exception;

	List<CompetitionFileDto> getCompetitionBeforeStart() throws Exception;

	CompetitionFileDto getCompetitionProgress() throws Exception;

	CompetitionFileDto getCompetitionProgress(Long userSeq) throws Exception;

	List<CompetitionFileDto> getCompetitionAfterEnd() throws Exception;

	List<CompetitionFileDto> getCompetitionAfterEnd(Long userSeq) throws Exception;

	Boolean checkCompetitionJoinable(Long competitionSeq, Long userSeq) throws Exception;

	Boolean joinCompetition(Long competitionSeq, Long userSeq) throws Exception;

	List<RankingDto> getCompetitionTotalRanking(Long competitionSeq, Long size, Long offset) throws Exception;

	RankingDto getCompetitionUserRanking(Long competitionSeq, Long userSeq) throws Exception;



}
