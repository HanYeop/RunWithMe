package com.ssafy.gumid101.res;

import java.util.List;

import com.ssafy.gumid101.dto.CrewTotalRecordDto;
import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Data
public class OtherUserFileDto {

	private UserDto user;
	private ImageFileDto imgFile;
	private CrewTotalRecordDto totalRecord;
	private List<AchieveCompleteDto> achieveList;
	
	
}
