package com.ssafy.gumid101.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.entity.UserSettingEntity;
import com.ssafy.gumid101.user.Role;

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
public class UserSettingDto implements Serializable {


	private Long userSettingSeq;

	private Long userSeq;
	
	private String nicknameColor;
	
	private String tracklineColor;
	
	public static UserSettingDto of(UserSettingEntity settingEntity) {
		
		if(settingEntity == null)
			return null;
		
		return new UserSettingDtoBuilder() //
				.userSettingSeq(settingEntity.getUserSettingSeq()) //
				.userSeq(settingEntity.getUserSeq()) //
				.nicknameColor(settingEntity.getNicknameColor()) //
				.tracklineColor(settingEntity.getTracklineColor()) //
				.build(); //
	}
	

}