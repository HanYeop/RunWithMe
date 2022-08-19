package com.ssafy.gumid101.dailycheck;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.DailyCheckEntity;

public interface DailyCheckRepository extends JpaRepository<DailyCheckEntity, Long>{
	
	Optional<DailyCheckEntity> findByUserSeqAndDailyCheckTimeBetween(Long userSeq, LocalDateTime dayStart, LocalDateTime dayEnd);
	Long countByUserSeqAndDailyCheckTimeBetween(Long userSeq, LocalDateTime monthStart, LocalDateTime monthEnd);
}
