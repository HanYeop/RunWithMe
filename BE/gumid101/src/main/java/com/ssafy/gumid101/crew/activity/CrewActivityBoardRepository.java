package com.ssafy.gumid101.crew.activity;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.CrewBoardEntity;
import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.res.CrewBoardFileDto;

public interface CrewActivityBoardRepository extends JpaRepository<CrewBoardEntity, Long> {
	List<CrewBoardEntity> findByCrewEntity(CrewEntity crewEntity, Pageable pageable);
	//List<CrewBoardEntity> findByCrewEntity(CrewEntity crewEntity);

	List<CrewBoardEntity> findByCrewEntityAndCrewBoardSeqLessThan(CrewEntity crewEntity,Long crewBoardSeq, Pageable pageable);

}
