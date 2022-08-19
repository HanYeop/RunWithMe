package com.ssafy.gumid101.dto;

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
public class LatLngParamsDto {
	@ApiParam(value = "위도(상)")
	private Double upperLat;
	@ApiParam(value = "위도(하)")
	private Double lowerLat;
	@ApiParam(value = "경도(좌)")
	private Double leftLng;
	@ApiParam(value = "경도(우)")
	private Double rightLng;
}
