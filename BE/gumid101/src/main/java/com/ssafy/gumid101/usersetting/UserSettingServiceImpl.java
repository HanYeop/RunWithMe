package com.ssafy.gumid101.usersetting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.gumid101.dto.UserSettingDto;
import com.ssafy.gumid101.entity.UserSettingEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSettingServiceImpl implements UserSettingService {
	private final UserSettingRepository settingRepo;

	@Override
	public UserSettingDto getUserSetting(Long userSeq) throws Exception {
		UserSettingEntity settingEntity = settingRepo.findByUserSeq(userSeq).orElse( //
				UserSettingEntity.builder() //
						.userSeq(userSeq) //
						.nicknameColor("#FFFFFF") //
						.tracklineColor("#FFFFFF") //
						.build());
		return UserSettingDto.of(settingEntity);
	}

	@Transactional
	@Override
	public UserSettingDto setUserSetting(Long userSeq, UserSettingDto settingDto) throws Exception {
		settingDto.setUserSettingSeq(null);
		settingDto.setUserSeq(userSeq);
		UserSettingEntity settingEntity = settingRepo.findByUserSeq(userSeq).orElse( //
				UserSettingEntity.of(settingDto));
		if (settingDto.getNicknameColor() != null) {
			settingEntity.setNicknameColor(settingDto.getNicknameColor());
		}
		if (settingDto.getTracklineColor() != null) {
			settingEntity.setTracklineColor(settingDto.getTracklineColor());
		}
		settingRepo.save(settingEntity);
		return UserSettingDto.of(settingEntity);
	}
}
