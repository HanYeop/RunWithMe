package com.ssafy.gumid101.res;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ScrapInfoDto {
	private Long scrapSeq;
	private String title;
	private TrackBoardFileDto trackBoardFileDto;
}