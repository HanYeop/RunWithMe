package com.ssafy.gumid101.dto;

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
public class LatLngParamsDto {
	private Double leftLng;
	private Double rightLng;
	private Double upperLat;
	private Double lowerLat;
}
