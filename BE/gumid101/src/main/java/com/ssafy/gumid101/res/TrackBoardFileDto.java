package com.ssafy.gumid101.res;

import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.TrackBoardDto;

import lombok.Data;

@Data
public class TrackBoardFileDto {
	private final TrackBoardDto trackBoardDto;
	private final ImageFileDto imageFileDto;
}
