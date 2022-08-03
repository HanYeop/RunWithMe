package com.ssafy.gumid101.crew;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.CrewTotalRecordEntity;
import com.ssafy.gumid101.entity.UserEntity;

public interface TotalRunRecordRepository extends JpaRepository<CrewTotalRecordEntity, Long>  {

	Optional<CrewTotalRecordEntity> findByUserEntityAndCrewEntity(UserEntity userEntity, CrewEntity crewEntity);
}
