package com.ssafy.gumid101.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 크루 가입 후 반환
 * @author start
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphRecordDto {
	private Integer amount;
	private Integer month;
	private Integer day;
}
