package com.ssafy.gumid101.scrap;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.ScrapEntity;
import com.ssafy.gumid101.entity.TrackBoardEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.res.ScrapInfoDto;

public interface ScrapRepository extends JpaRepository<ScrapEntity, Long>{
	Optional<ScrapEntity> findByUserEntityAndTrackBoardEntity(UserEntity userEntity, TrackBoardEntity trackBoardEntity);
	Collection<ScrapEntity> findByUserEntityAndScrapTitleContaining(UserEntity userEntity, String title);
}
