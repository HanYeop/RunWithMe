package com.ssafy.gumid101.dto;

import java.io.Serializable;

import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.user.Role;

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
public class RankingParamsDto implements Serializable {
	private long userSeq;
	
	private long crewSeq;
	
	private int size;
	
	private int offset;
	
	private String type;
}