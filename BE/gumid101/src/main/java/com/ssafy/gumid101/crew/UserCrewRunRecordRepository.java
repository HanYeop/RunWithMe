package com.ssafy.gumid101.crew;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.CrewTotalRecordEntity;
import com.ssafy.gumid101.entity.UserEntity;

public interface UserCrewRunRecordRepository extends JpaRepository<CrewTotalRecordEntity, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select ctr from CrewTotalRecordEntity ctr where ctr.userEntity = :userEntity AND ctr.crewEntity = :crewEntity")
	CrewTotalRecordEntity findWithLockByUserEntityAndCrewEntity(UserEntity userEntity, CrewEntity crewEntity);

}
