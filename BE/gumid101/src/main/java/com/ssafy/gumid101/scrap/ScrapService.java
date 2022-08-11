package com.ssafy.gumid101.scrap;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.gumid101.res.ScrapInfoDto;

@Service
public interface ScrapService {

	ScrapInfoDto scrapTrackBoard(Long userSeq, Long trackBoardSeq, String title) throws Exception;

	List<ScrapInfoDto> getScraps(Long userSeq, String title) throws Exception;

	Boolean deleteScrap(Long userSeq, Long scrapSeq) throws Exception;
}
