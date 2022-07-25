package com.ssafy.gumid101.crew.activity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.CrewBoardEntity;
import com.ssafy.gumid101.entity.CrewEntity;

public interface CrewActivityBoardRepository extends JpaRepository<CrewBoardEntity, Long> {
//	List<CrewBoardEntity> findByCrewEntity(Long crewSeq, Pageable pageable);
	List<CrewBoardEntity> findByCrewEntity(CrewEntity crewEntity);
	
}
