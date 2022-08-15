package com.ssafy.gumid101.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ssafy.gumid101.dto.UserSettingDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_user_setting")
@NoArgsConstructor
public class UserSettingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_setting_seq")
	private Long userSettingSeq;

	@Column(nullable = false, name = "user_seq")
	private Long userSeq;

	@Column(nullable = false, name = "nickname_color")
	private String nicknameColor;

	@Column(nullable = false, name = "trackline_color")
	private String tracklineColor;

	@PrePersist
	public void setting() {
		this.nicknameColor = "#FFFFFF";
		this.tracklineColor = "#FFFFFF";
	}

	public static UserSettingEntity of(UserSettingDto settingDto) {
		if (settingDto == null) {
			return null;
		}
		return new UserSettingEntityBuilder() //
				.userSettingSeq(settingDto.getUserSettingSeq()) //
				.userSeq(settingDto.getUserSeq()) //
				.nicknameColor(settingDto.getNicknameColor()) //
				.tracklineColor(settingDto.getTracklineColor()) //
				.build(); //
	}
}