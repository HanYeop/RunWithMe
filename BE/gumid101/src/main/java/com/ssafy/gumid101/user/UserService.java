package com.ssafy.gumid101.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.dto.CrewTotalRecordDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.res.CrewBoardFileDto;
import com.ssafy.gumid101.res.CrewBoardRes;
import com.ssafy.gumid101.res.UserFileDto;

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

	UserFileDto getUserFileDtoById(Long id) throws Exception;
	
	UserFileDto getUserProfileByNickname(String nickname) throws Exception;

	UserFileDto editMyProfile(UserDto userDto, MultipartFile imgFile)throws Exception;

	List<CrewBoardFileDto> getMyBoards(Long userSeq, Long size, Long offset) throws Exception;

	CrewTotalRecordDto getMyTotalRecord(Long userSeq) throws Exception;

	boolean setUserFcmToken(Long userSeq, String string)throws Exception;

	boolean deleteUserFcmToken(Long userSeq) throws Exception;

	boolean deleteMyAccount(Long userSeq)throws Exception;



}
