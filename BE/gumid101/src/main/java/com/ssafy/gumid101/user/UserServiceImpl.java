package com.ssafy.gumid101.user;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.ssafy.gumid101.customexception.DuplicateException;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.UserEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepo;

	@Transactional
	@Override
	public UserDto setMyProfile(UserDto userDto) throws Exception {

		if (0 != userRepo.countByEmail(userDto.getEmail())) {
			// 이미 해당 이메일이 있으면
			throw new DuplicateException(String.format("%s은 이미 등록된 이메일 입니다.", userDto.getEmail()));
		}

		int kk = userDto.getWeight();
		Integer k = userDto.getWeight();

		UserEntity userEntity = UserEntity.builder().email(userDto.getEmail()).nickName(userDto.getNickName())
				.weight(userDto.getWeight()).height(userDto.getHeight()).role(Role.USER).build();
		
		try {
			userRepo.save(userEntity);	
		}catch(Exception e) {
			throw new Exception("초기 프로필 설정 중 오류가 발생하였습니다.");
		}
		

		return UserDto.of(userEntity);
	}

	@Override
	public int checkDupNickname(String nickname) throws Exception {

		return userRepo.countByNickName(nickname);

	}

	@Override
	public UserDto getUserProfileById(Long id) throws Exception {

		UserEntity user = userRepo.findById(id).orElse(null);

		UserDto userDto = null;
		if (user != null) {
			userDto = UserDto.of(user);
		}

		return userDto;
	}

}
