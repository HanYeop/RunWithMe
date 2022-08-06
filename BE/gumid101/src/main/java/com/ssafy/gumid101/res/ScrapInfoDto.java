package com.ssafy.gumid101.res;

import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.TrackBoardDto;
import com.ssafy.gumid101.dto.UserDto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ScrapInfoDto {
	private Long scrapSeq;
	private String title;
	private UserDto runnerDto;
	private RunRecordDto recordDto;
	private TrackBoardDto trackBoardDto;
	private ImageFileDto imageFileDto;
}