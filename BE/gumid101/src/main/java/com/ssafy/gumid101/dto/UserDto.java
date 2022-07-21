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
public class UserDto implements Serializable {

	private Long id;

	private String nickName;

	private String email;

	private Integer height;

	private Integer weight;

	private Integer point;

	private String fcmToken;

	private String userState;

	private Role role;

	public static UserDto of(UserEntity user) {
		return new UserDtoBuilder().id(user.getId()).nickName(user.getNickName()).email(user.getEmail())
				.height(user.getHeight()).weight(user.getWeight()).point(user.getPoint()).fcmToken(user.getFcmToken())
				.userState(user.getUserState()).role(user.getRole()).build();
	}
	

}