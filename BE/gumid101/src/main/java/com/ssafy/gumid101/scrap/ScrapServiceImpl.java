package com.ssafy.gumid101.scrap;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ssafy.gumid101.customexception.DuplicateException;
import com.ssafy.gumid101.customexception.IllegalParameterException;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.ScrapEntity;
import com.ssafy.gumid101.entity.TrackBoardEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.recommend.RecommendRepository;
import com.ssafy.gumid101.res.ScrapInfoDto;
import com.ssafy.gumid101.res.TrackBoardFileDto;
import com.ssafy.gumid101.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ScrapServiceImpl implements ScrapService {

	private final UserRepository userRepo;
	private final RecommendRepository recoRepo;
	private final ScrapRepository scrapRepo;

	@Override
	public ScrapInfoDto scrapTrackBoard(Long userSeq, Long trackBoardSeq, String title) throws Exception {
		UserEntity userEntity = userRepo.findById(userSeq)
				.orElseThrow(() -> new NotFoundUserException("유저 정보가 올바르지 않습니다."));
		TrackBoardEntity trackBoardEntity = recoRepo.findById(trackBoardSeq)
				.orElseThrow(() -> new NotFoundUserException("추천 경로 정보가 올바르지 않습니다."));

		Optional<ScrapEntity> dupCheck = scrapRepo.findByUserEntityAndTrackBoardEntity(userEntity, trackBoardEntity);
		if (dupCheck.isPresent()) {
			throw new DuplicateException("이미 스크랩한 경로입니다.");
		}
		ScrapEntity scrapEntity = ScrapEntity.builder() //
				.userEntity(userEntity) //
				.trackBoardEntity(trackBoardEntity) //
				.scrapTitle(title) //
				.build(); //
		scrapRepo.save(scrapEntity);
		UserDto runnerDto = UserDto.of(trackBoardEntity.getRunRecordEntity().getUserEntity());
		runnerDto.hideInfo();
		return ScrapInfoDto.builder() //
				.scrapSeq(scrapEntity.getScrapSeq()) //
				.title(title == null ? "" : title) //
				.trackBoardFileDto(TrackBoardFileDto.of(trackBoardEntity)) //
				.build(); //
	}

	@Override
	public Boolean deleteScrap(Long userSeq, Long scrapSeq) throws Exception {
		UserEntity userEntity = userRepo.findById(userSeq)
				.orElseThrow(() -> new NotFoundUserException("유저 정보가 올바르지 않습니다."));
		ScrapEntity scrapEntity = scrapRepo.findById(scrapSeq)
				.orElseThrow(() -> new NotFoundUserException("스크랩 정보를 찾을 수 없습니다."));
		if (userEntity.getUserSeq() != scrapEntity.getUserEntity().getUserSeq()) {
			throw new IllegalParameterException("자신의 스크랩만 삭제할 수 있습니다.");
		}
		scrapRepo.delete(scrapEntity);
		return true;
	}

	@Override
	public List<ScrapInfoDto> getScraps(Long userSeq, String title) throws Exception {
		UserEntity userEntity = userRepo.findById(userSeq)
				.orElseThrow(() -> new NotFoundUserException("유저 정보가 올바르지 않습니다."));

		List<ScrapInfoDto> scrapList = scrapRepo.findByUserEntityAndScrapTitleContaining(userEntity, title).stream()
				.map((entity) -> {
					return ScrapInfoDto.builder() //
							.scrapSeq(entity.getScrapSeq()) //
							.title(entity.getScrapTitle()) //
							.trackBoardFileDto(TrackBoardFileDto.of(entity.getTrackBoardEntity())) //
							.build();
				}).collect(Collectors.toList());
		return scrapList;
	}
}
