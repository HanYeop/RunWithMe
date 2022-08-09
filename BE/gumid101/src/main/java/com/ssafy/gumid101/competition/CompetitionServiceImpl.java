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
import com.ssafy.gumid101.entity.CompetitionTotalRecordEntity;
import com.ssafy.gumid101.entity.CompetitionUserEntity;
import com.ssafy.gumid101.entity.ImageFileEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.imgfile.ImageDirectory;
import com.ssafy.gumid101.imgfile.ImageFileRepository;
import com.ssafy.gumid101.res.CompetitionFileDto;
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
	private final CompetitionUserRepository competitionUserRepo;
	private final CompetitionTotalRecordRepository competitionTotalRecordRepo;
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
		List<CompetitionFileDto> competitionFileDtolist = competitionRepo.findByCompetitionDateStartAfter(LocalDateTime.now())
				.stream().map((entity) -> {
					return CompetitionFileDto.of(entity);
				}).collect(Collectors.toList());
		return competitionFileDtolist;
	}

	@Override
	public List<CompetitionFileDto> getCompetitionProgress() throws Exception {
		List<CompetitionFileDto> competitionFileDtolist = competitionRepo
				.findByCompetitionDateStartBeforeAndCompetitionDateEndAfter(LocalDateTime.now(), LocalDateTime.now())
				.stream().map((entity) -> {
					return CompetitionFileDto.of(entity);
				}).collect(Collectors.toList());
		return competitionFileDtolist;
	}

	@Override
	public List<CompetitionFileDto> getCompetitionProgress(Long userSeq) throws Exception {
		Set<Long> competitionSeqSet = competitionUserRepo.findByUserEntity_userSeq(userSeq).stream().map((entity) -> {
			return entity.getCompetitionEntity().getCompetitionSeq();
		}).collect(Collectors.toSet());
		List<CompetitionFileDto> competitionFileDtolist = new ArrayList<>();
		for (CompetitionEntity competitionEntity : competitionRepo
				.findByCompetitionDateStartBeforeAndCompetitionDateEndAfter(LocalDateTime.now(), LocalDateTime.now())) {
			if (competitionSeqSet.contains(competitionEntity.getCompetitionSeq())) {
				competitionFileDtolist.add(CompetitionFileDto.of(competitionEntity));
			}
		}
		return competitionFileDtolist;
	}

	@Override
	public List<CompetitionFileDto> getCompetitionAfterEnd() throws Exception {
		List<CompetitionFileDto> competitionFileDtolist = competitionRepo.findByCompetitionDateEndBefore(LocalDateTime.now())
				.stream().map((entity) -> {
					return CompetitionFileDto.of(entity);
				}).collect(Collectors.toList());
		return competitionFileDtolist;
	}

	@Override
	public List<CompetitionFileDto> getCompetitionAfterEnd(Long userSeq) throws Exception {
		Set<Long> competitionSeqSet = competitionUserRepo.findByUserEntity_userSeq(userSeq).stream().map((entity) -> {
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
		if (competitionUserRepo.findByUserEntityAndCompetitionEntity(userEntity, competitionEntity).isPresent()) {
			return false;
		}
		return true;
	}
	
	
	@Transactional
	@Override
	public Boolean joinCompetition(Long competitionSeq, Long userSeq) throws Exception{
		UserEntity userEntity = userRepo.findById(userSeq)
				.orElseThrow(() -> new NotFoundUserException("해당 유저를 찾을 수 없습니다."));
		CompetitionEntity competitionEntity = competitionRepo.findById(competitionSeq)
				.orElseThrow(() -> new NotFoundUserException("해당 대회를 찾을 수 없습니다."));
		if (competitionEntity.getCompetitionDateEnd().isBefore(LocalDateTime.now())) {
			throw new IllegalParameterException("이미 종료된 대회입니다.");
		}
		if (competitionUserRepo.findByUserEntityAndCompetitionEntity(userEntity, competitionEntity).isPresent()) {
			throw new DuplicateException("이미 참가한 대회입니다.");
		}
		CompetitionUserEntity competitionUserEntity = CompetitionUserEntity.builder() //
				.userEntity(userEntity) //
				.competitionEntity(competitionEntity) //
				.checkYn("N") //
				.build();
		CompetitionTotalRecordEntity competitionTotalRecordEntity = CompetitionTotalRecordEntity.builder() //
				.userEntity(userEntity) //
				.competitionEntity(competitionEntity) //
				.competition_distance(0) //
				.competition_time(0) //
				.build();
		competitionUserRepo.save(competitionUserEntity);
		competitionTotalRecordRepo.save(competitionTotalRecordEntity);
		return true;
	}

}
