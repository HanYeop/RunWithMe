package com.ssafy.gumid101.res;

import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.TrackBoardDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.TrackBoardEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TrackBoardFileDto {
	private final TrackBoardDto trackBoardDto;
	private final ImageFileDto trackBoardImageFileDto;
	private final UserDto userDto;
	private final RunRecordDto runRecordDto;
	private final ImageFileDto imageFileDto;

	public static TrackBoardFileDto of(TrackBoardEntity trackBoardEntity) {
		if (trackBoardEntity == null) {
			return null;
		}
		UserDto userDto = UserDto.of(trackBoardEntity.getRunRecordEntity().getUserEntity());
		userDto.setEmail(null);
		userDto.setFcmToken(null);
		userDto.setRole(null);
		return TrackBoardFileDto.builder() //
				.trackBoardDto(TrackBoardDto.of(trackBoardEntity)) //
				.trackBoardImageFileDto(ImageFileDto.of(trackBoardEntity.getTrackBoardImageEntity())) //
				.userDto(userDto) //
				.runRecordDto(RunRecordDto.of(trackBoardEntity.getRunRecordEntity())) //
				.imageFileDto(ImageFileDto.of(trackBoardEntity.getRunRecordEntity().getImageFile())) //
				.build(); //
	}
}
