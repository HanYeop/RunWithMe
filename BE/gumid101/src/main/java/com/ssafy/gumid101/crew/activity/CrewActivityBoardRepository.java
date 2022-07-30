package com.ssafy.gumid101.crew.activity;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.gumid101.entity.CrewBoardEntity;
import com.ssafy.gumid101.entity.CrewEntity;

public interface CrewActivityBoardRepository extends JpaRepository<CrewBoardEntity, Long> {
	List<CrewBoardEntity> findByCrewEntity(CrewEntity crewEntity, Pageable pageable);
	// List<CrewBoardEntity> findByCrewEntity(CrewEntity crewEntity);

	@Query("SELECT c FROM CrewBoardEntity c WHERE c.crewEntity = :crewEntity AND c.crewBoardSeq < :maxCrewBoardSeq")
	List<CrewBoardEntity> findByCrewEntityAndCrewBoardSeqLessThan(
			CrewEntity crewEntity, Long maxCrewBoardSeq, Pageable pageable);

}
