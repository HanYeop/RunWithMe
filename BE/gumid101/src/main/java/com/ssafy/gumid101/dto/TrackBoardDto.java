package com.ssafy.gumid101.dto;

import java.io.Serializable;

import com.ssafy.gumid101.entity.TrackBoardEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 직렬화 기능을 가진 User클래스
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackBoardDto implements Serializable {

	private long trackBoardSeq;
	
	private int trackBoardHardPoint;
	
	private int trackBoardEnvironmentPoint;
	
	public static TrackBoardDto of(TrackBoardEntity trackBoard) {
		return new TrackBoardDtoBuilder()
				.trackBoardSeq(trackBoard.getTrackBoardSeq())
				.trackBoardHardPoint(trackBoard.getTrackBoardHardPoint())
				.trackBoardEnvironmentPoint(trackBoard.getTrackBoardEnviromentPoint())
				.build();
	}
}