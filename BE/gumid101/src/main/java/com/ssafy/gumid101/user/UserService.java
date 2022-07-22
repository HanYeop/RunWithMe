package com.ssafy.gumid101.user;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.customexception.DuplicateException;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.req.ProfileEditDto;

@Service
public interface UserService {

	/**
	 * 초기 프로필 설정
	 * @param tokenUser
	 * @return
	 * @throws Exception
	 */
	UserDto setMyProfile(UserDto userDto) throws Exception;
	
	/**
	 * 닉네임 중복확인
	 * @param nickname
	 * @return
	 * @throws Exception
	 */
	int checkDupNickname(String nickname) throws Exception;

	UserDto getUserProfileById(Long id)throws Exception;

	boolean editMyProfile(UserDto userDto, MultipartFile imgFile)throws Exception;


}
