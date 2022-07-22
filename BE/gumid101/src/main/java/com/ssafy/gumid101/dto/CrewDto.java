package com.ssafy.gumid101.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.ssafy.gumid101.entity.CrewEntity;

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
public class CrewDto implements Serializable {

	private Long crewSeq;

	private String crewName;
	
	private String crewDescription;
	
	private Integer crewGoalDays;
	
	private String crewGoalType;
	
	private Integer crewGoalAmount;
	
	private LocalDateTime crewDateStart;
	
	private LocalDateTime crewDateEnd;
	
	private LocalTime crewTimeStart;

	private LocalTime crewTimeEnd;

	private String crewPassword;
	
	private Integer crewCost;
	
	private Integer crewMaxMember;
	
	public static CrewDto of(CrewEntity crew) {
		return new CrewDtoBuilder().crewSeq(crew.getCrewSeq()).crewCost(crew.getCrewCost()).crewDateEnd(crew.getCrewDateEnd()).crewDateStart(crew.getCrewDateEnd())
				.crewDescription(crew.getCrewDescription()).crewGoalAmount(crew.getCrewGoalAmount()).crewGoalDays(crew.getCrewGoalDays())
				.crewGoalType(crew.getCrewGoalType()).crewMaxMember(crew.getCrewMaxMember()).crewName(crew.getCrewName()).crewPassword(crew.getCrewPassword())
				.crewTimeEnd(crew.getCrewTimeEnd()).crewTimeStart(crew.getCrewTimeStart()).build();
	}
	

}