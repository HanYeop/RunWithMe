package com.ssafy.gumid101.res;

import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.TrackBoardDto;
import com.ssafy.gumid101.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TrackBoardFileDto {
	private final TrackBoardDto trackBoardDto;
	private final UserDto userDto;
	private final RunRecordDto runRecordDto;
	private final ImageFileDto imageFileDto;
}
