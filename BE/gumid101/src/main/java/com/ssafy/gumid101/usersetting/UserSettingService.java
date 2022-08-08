package com.ssafy.gumid101.usersetting;

import org.springframework.stereotype.Service;

import com.ssafy.gumid101.dto.UserSettingDto;

@Service
public interface UserSettingService {

	UserSettingDto getUserSetting(Long userSeq) throws Exception;

	UserSettingDto setUserSetting(Long userSeq, UserSettingDto settingDto) throws Exception;

}
