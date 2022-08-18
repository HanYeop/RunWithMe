package com.ssafy.gumid101.competition;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.aws.S3FileService;
import com.ssafy.gumid101.customexception.DuplicateException;
import com.ssafy.gumid101.customexception.IllegalParameterException;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.customexception.ThirdPartyException;
import com.ssafy.gumid101.dto.CompetitionDto;
import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.entity.CompetitionEntity;
import com.ssafy.gumid101.entity.CompetitionUserRecordEntity;
import com.ssafy.gumid101.entity.ImageFileEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.imgfile.ImageDirectory;
import com.ssafy.gumid101.imgfile.ImageFileRepository;
import com.ssafy.gumid101.res.CompetitionFileDto;
import com.ssafy.gumid101.res.RankingDto;
import com.ssafy.gumid101.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {

	private final UserRepository userRepo;
	private final S3FileService s3Serv;
	private final CompetitionRepository competitionRepo;
	private final CompetitionUserRecordRepository competitionUserRecordRepo;
	private final ImageFileRepository imageRepo;

	@Transactional
	@Override
	public CompetitionFileDto makeCompetition(CompetitionDto competitionDto, MultipartFile competitionImageFile)
			throws Exception {
		if (competitionRepo.countByCompetitionDateStartBetweenOrCompetitionDateEndBetween(
				competitionDto.getCompetitionDateStart(), competitionDto.getCompetitionDateEnd(),
				competitionDto.getCompetitionDateStart(), competitionDto.getCompetitionDateEnd()) > 0) {
			throw new DuplicateException("기간이 겹치는 대회가 이미 있습니다.");
		}
		CompetitionEntity competitionEntity = CompetitionEntity.of(competitionDto);
		competitionEntity.setCompetitionSeq(null);
		if (competitionImageFile != null) {
			try {
				ImageFileDto savedFileDto = s3Serv.upload(competitionImageFile,
						ImageDirectory.COMPETITION_LOGO.getPath());
				// 이미지쪽 세이브
				ImageFileEntity imageEntity = ImageFileEntity.builder() //
						.imgOriginalName(savedFileDto.getImgOriginalName()) //
						.imgSavedName(savedFileDto.getImgSavedName()) //
						.imgSavedPath(savedFileDto.getImgSavedPath()) //
						.build(); //
				imageRepo.save(imageEntity);
				competitionEntity.setCompetitionImageFile(imageEntity);
			} catch (Exception e) {
				throw new ThirdPartyException("이미지 저장에 실패했습니다.");
			}
		}
		competitionRepo.save(competitionEntity);
		return CompetitionFileDto.of(competitionEntity);
	}

	@Override
	public List<CompetitionFileDto> getCompetitionBeforeStart() throws Exception {
		List<CompetitionFileDto> competitionFileDtolist = competitionRepo
				.findByCompetitionDateStartAfter(LocalDateTime.now()).stream().map((entity) -> {
					return CompetitionFileDto.of(entity);
				}).collect(Collectors.toList());
		return competitionFileDtolist;
	}

	@Override
	public CompetitionFileDto getCompetitionProgress() throws Exception {
		CompetitionEntity competitionEntity = competitionRepo
				.findByCompetitionDateStartBeforeAndCompetitionDateEndAfter(LocalDateTime.now(), LocalDateTime.now())
				.orElseThrow(() -> new NotFoundUserException("진행중인 대회가 없습니다."));
		return CompetitionFileDto.of(competitionEntity);
	}

	@Override
	public CompetitionFileDto getCompetitionProgress(Long userSeq) throws Exception {
		Set<Long> competitionSeqSet = competitionUserRecordRepo.findByUserEntity_userSeq(userSeq).stream()
				.map((entity) -> {
					return entity.getCompetitionEntity().getCompetitionSeq();
				}).collect(Collectors.toSet());
		CompetitionEntity competitionEntity = competitionRepo
				.findByCompetitionDateStartBeforeAndCompetitionDateEndAfter(LocalDateTime.now(), LocalDateTime.now())
				.orElseThrow(() -> new NotFoundUserException("진행중인 대회가 없습니다."));
		if (competitionSeqSet.contains(competitionEntity.getCompetitionSeq())) {
			return CompetitionFileDto.of(competitionEntity);
		} else {
			throw new NotFoundUserException("진행중인 대회에 참가하고 있지 않습니다.");
		}
	}

	@Override
	public List<CompetitionFileDto> getCompetitionAfterEnd() throws Exception {
		List<CompetitionFileDto> competitionFileDtolist = competitionRepo
				.findByCompetitionDateEndBefore(LocalDateTime.now()).stream().map((entity) -> {
					return CompetitionFileDto.of(entity);
				}).collect(Collectors.toList());
		return competitionFileDtolist;
	}

	@Override
	public List<CompetitionFileDto> getCompetitionAfterEnd(Long userSeq) throws Exception {
		Set<Long> competitionSeqSet = competitionUserRecordRepo.findByUserEntity_userSeq(userSeq).stream()
				.map((entity) -> {
					return entity.getCompetitionEntity().getCompetitionSeq();
				}).collect(Collectors.toSet());
		List<CompetitionFileDto> competitionFileDtolist = new ArrayList<>();
		for (CompetitionEntity competitionEntity : competitionRepo
				.findByCompetitionDateEndBefore(LocalDateTime.now())) {
			if (competitionSeqSet.contains(competitionEntity.getCompetitionSeq())) {
				competitionFileDtolist.add(CompetitionFileDto.of(competitionEntity));
			}
		}
		return competitionFileDtolist;
	}

	@Override
	public Boolean checkCompetitionJoinable(Long competitionSeq, Long userSeq) throws Exception {
		UserEntity userEntity = userRepo.findById(userSeq)
				.orElseThrow(() -> new NotFoundUserException("해당 유저를 찾을 수 없습니다."));
		CompetitionEntity competitionEntity = competitionRepo.findById(competitionSeq)
				.orElseThrow(() -> new NotFoundUserException("해당 대회를 찾을 수 없습니다."));
		if (competitionEntity.getCompetitionDateEnd().isBefore(LocalDateTime.now())) {
			throw new IllegalParameterException("이미 종료된 대회입니다.");
		}
		if (competitionUserRecordRepo.findByUserEntityAndCompetitionEntity(userEntity, competitionEntity).isPresent()) {
			return false;
		}
		return true;
	}
	
	@Override
	public Long countParticipantCompetition(Long competitionSeq) throws Exception {
		CompetitionEntity competitionEntity = competitionRepo.findById(competitionSeq)
				.orElseThrow(() -> new NotFoundUserException("해당 대회를 찾을 수 없습니다."));
		
		return competitionUserRecordRepo.countByCompetitionEntity(competitionEntity);
	}

	@Transactional
	@Override
	public Boolean joinCompetition(Long competitionSeq, Long userSeq) throws Exception {
		UserEntity userEntity = userRepo.findById(userSeq)
				.orElseThrow(() -> new NotFoundUserException("해당 유저를 찾을 수 없습니다."));
		CompetitionEntity competitionEntity = competitionRepo.findById(competitionSeq)
				.orElseThrow(() -> new NotFoundUserException("해당 대회를 찾을 수 없습니다."));
		if (competitionEntity.getCompetitionDateEnd().isBefore(LocalDateTime.now())) {
			throw new IllegalParameterException("이미 종료된 대회입니다.");
		}
		if (competitionUserRecordRepo.findByUserEntityAndCompetitionEntity(userEntity, competitionEntity).isPresent()) {
			throw new DuplicateException("이미 참가한 대회입니다.");
		}
		CompetitionUserRecordEntity competitionUserRecordEntity = CompetitionUserRecordEntity.builder() //
				.userEntity(userEntity) //
				.competitionEntity(competitionEntity) //
				.competitionDistance(0) //
				.competitionTime(0) //
				.build();
		competitionUserRecordRepo.save(competitionUserRecordEntity);
		return true;
	}

	@Override
	public List<RankingDto> getCompetitionTotalRanking(Long competitionSeq, Long size, Long offset) throws Exception {
		CompetitionEntity competitionEntity = competitionRepo.findById(competitionSeq)
				.orElseThrow(() -> new NotFoundUserException("해당 대회를 찾을 수 없습니다."));
		return competitionRepo.getCompetitionRankingList(size, offset);
	}
	
	@Override
	public RankingDto getCompetitionUserRanking(Long competitionSeq, Long userSeq) throws Exception{
		UserEntity userEntity = userRepo.findById(userSeq)
				.orElseThrow(() -> new NotFoundUserException("해당 유저를 찾을 수 없습니다."));
		CompetitionEntity competitionEntity = competitionRepo.findById(competitionSeq)
				.orElseThrow(() -> new NotFoundUserException("해당 대회를 찾을 수 없습니다."));
		CompetitionUserRecordEntity competitionUserRecordEntity = competitionUserRecordRepo.findByUserEntityAndCompetitionEntity(userEntity, competitionEntity)
				.orElseThrow(() -> new NotFoundUserException("해당 유저는 해당 대회에 참여하지 않았습니다."));
		return competitionRepo.getCompetitionUserRanking(competitionSeq, userSeq);
	}

}
