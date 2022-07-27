package com.ssafy.gumid101.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.gumid101.entity.CrewEntity;

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
public class CrewDto implements Serializable {

	private Long crewSeq;

	@ApiParam(value = "크루 이름", required = true)
	private String crewName;
	
	@ApiParam(value = "크루 설명")
	private String crewDescription;
	
	@ApiParam(value = "한 주당 목표 활동일수", required = true)
	private Integer crewGoalDays;
	
	@ApiParam(value = "크루 목표 형태", required = true)
	private String crewGoalType;
	
	@ApiParam(value = "크루 목표 수치", required = true)
	private Integer crewGoalAmount;
	
	@ApiParam(value = "크루 활동 시작 날짜. (yyyy-MM-dd 00:00:00)", required = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime crewDateStart;
	
	@ApiParam(value = "크루 활동 종료 날짜. (yyyy-MM-dd 23:59:59)", required = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime crewDateEnd;
	
	@ApiParam(value = "크루 활동 시작 시간. (HH:mm:ss)", required = true)
	@DateTimeFormat(pattern = "HH:mm:ss")
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime crewTimeStart;

	@ApiParam(value = "크루 활동 종료 시간. (HH:mm:ss)", required = true)
	@DateTimeFormat(pattern = "HH:mm:ss")
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime crewTimeEnd;

	@ApiParam(value = "크루 비밀번호.")
	private String crewPassword;
	
	@ApiParam(value = "참가비")
	private Integer crewCost;
	
	@ApiParam(value = "크루 인원 제한", required = true)
	private Integer crewMaxMember;
	
	@ApiParam(value = "크루장 닉네임")
	private String crewManagerNickName;
	
	@ApiParam(value = "크루장 SEQ")
	private Long crewManagerSeq;
	
	public static CrewDto of(CrewEntity crew) {
		if(crew == null)
			return null;
		return new CrewDtoBuilder().crewSeq(crew.getCrewSeq()).crewCost(crew.getCrewCost()).crewDateEnd(crew.getCrewDateEnd()).crewDateStart(crew.getCrewDateStart())
				.crewDescription(crew.getCrewDescription()).crewGoalAmount(crew.getCrewGoalAmount()).crewGoalDays(crew.getCrewGoalDays())
				.crewGoalType(crew.getCrewGoalType()).crewMaxMember(crew.getCrewMaxMember()).crewName(crew.getCrewName()).crewPassword(crew.getCrewPassword())
				.crewTimeEnd(crew.getCrewTimeEnd()).crewTimeStart(crew.getCrewTimeStart()).build();
	}
	
	
	public static CrewDto of(CrewEntity crew,String crewManagerNickName, Long crewManagerSeq) {
		if(crew == null)
			return null;
		
		CrewDto dto = new CrewDtoBuilder().crewSeq(crew.getCrewSeq()).crewCost(crew.getCrewCost()).crewDateEnd(crew.getCrewDateEnd()).crewDateStart(crew.getCrewDateStart())
		.crewDescription(crew.getCrewDescription()).crewGoalAmount(crew.getCrewGoalAmount()).crewGoalDays(crew.getCrewGoalDays())
		.crewGoalType(crew.getCrewGoalType()).crewMaxMember(crew.getCrewMaxMember()).crewName(crew.getCrewName()).crewPassword(crew.getCrewPassword())
		.crewTimeEnd(crew.getCrewTimeEnd()).crewTimeStart(crew.getCrewTimeStart()).build();
		
		dto.setCrewManagerNickName(crewManagerNickName);
		dto.setCrewManagerSeq(crewManagerSeq);
		
		return dto; 
	}

}