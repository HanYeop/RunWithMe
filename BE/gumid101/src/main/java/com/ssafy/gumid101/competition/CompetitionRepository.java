package com.ssafy.gumid101.competition;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.CompetitionEntity;

public interface CompetitionRepository extends JpaRepository<CompetitionEntity, Long>{
	Long countByCompetitionDateStartBetweenOrCompetitionDateEndBetween(LocalDateTime startS, LocalDateTime startE, LocalDateTime endS, LocalDateTime endE);
	List<CompetitionEntity> findByCompetitionDateStartAfter(LocalDateTime now);
	List<CompetitionEntity> findByCompetitionDateStartBeforeAndCompetitionDateEndAfter(LocalDateTime now1, LocalDateTime now2);
	List<CompetitionEntity> findByCompetitionDateEndBefore(LocalDateTime now);
}
