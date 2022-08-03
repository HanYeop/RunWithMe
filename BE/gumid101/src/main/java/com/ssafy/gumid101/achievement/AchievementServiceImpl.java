package com.ssafy.gumid101.achievement;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.dto.AchievementDto;
import com.ssafy.gumid101.entity.AchievementEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.res.MyAchieveCompleteDto;
import com.ssafy.gumid101.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AchievementServiceImpl implements AchievementService {
	
	private final UserRepository userRepo;
	private final AchievementCompleteRepository completeRepo; 
	private final AchievementRepository achieveRepo; 
	
	@Override
	public List<AchievementDto> getAchieveList() throws Exception{	
		return achieveRepo.findAll().stream().map((entity) -> {
			return AchievementDto.of(entity);
		}).collect(Collectors.toList());
	}

	@Override
	public List<MyAchieveCompleteDto> getUserAchievement(Long userSeq) throws Exception{
		UserEntity user = userRepo.findById(userSeq).orElseThrow(() -> {
			return new NotFoundUserException("유저 정보가 올바르지 않습니다.");
		});
		return completeRepo.findByUserSeq(user.getUserSeq()).stream().map((entity) -> {
			return new MyAchieveCompleteDto(AchievementDto.of(entity.getAchieveEntity()), entity.getAchieveCompleteRegTime());
		}).collect(Collectors.toList());
	}
	
	@Transactional
	@Override
	public void checkAchievement(String acName, AchieveType acType, Double acValue) {
		Optional<AchievementEntity> acEntityO = achieveRepo.findByAchieveNameAndAchieveTypeAndAchiveValue(acName, acType, (Double) acValue);
		if (!acEntityO.isPresent()) {
			achieveRepo.save(AchievementEntity.builder()
					.achieveName(acName)
					.achieveType(acType)
					.achiveValue(acValue)
					.build());
		}
	}
}
