package com.ssafy.gumid101.dto;

import java.io.Serializable;

import org.springframework.format.annotation.DateTimeFormat;

import com.ssafy.gumid101.entity.TrackBoardEntity;

import io.swagger.annotations.ApiParam;
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

	private Long trackBoardSeq;

	@ApiParam(value = "난이도 별점 (0 ~ 5)")
	private Integer trackBoardHardPoint;
	
	@ApiParam(value = "주변 환경 별점 (0 ~ 5)")
	private Integer trackBoardEnvironmentPoint;
	
	public static TrackBoardDto of(TrackBoardEntity trackBoard) {
		return new TrackBoardDtoBuilder()
				.trackBoardSeq(trackBoard.getTrackBoardSeq())
				.trackBoardHardPoint(trackBoard.getTrackBoardHardPoint())
				.trackBoardEnvironmentPoint(trackBoard.getTrackBoardEnviromentPoint())
				.build();
	}
}