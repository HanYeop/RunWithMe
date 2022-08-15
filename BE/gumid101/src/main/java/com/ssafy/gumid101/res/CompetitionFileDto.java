package com.ssafy.gumid101.res;

import com.ssafy.gumid101.dto.CompetitionDto;
import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.entity.CompetitionEntity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CompetitionFileDto {
	
	
	private CompetitionDto competitionDto;
	
	private ImageFileDto competitionImageFileDto;
	
	public static CompetitionFileDto of(CompetitionEntity competitionEntity) {
		if (competitionEntity == null) {
			return null;
		}
		return CompetitionFileDto.builder() //
				.competitionDto(CompetitionDto.of(competitionEntity)) //
				.competitionImageFileDto(ImageFileDto.of(competitionEntity.getCompetitionImageFile())) //
				.build();
	}
}
