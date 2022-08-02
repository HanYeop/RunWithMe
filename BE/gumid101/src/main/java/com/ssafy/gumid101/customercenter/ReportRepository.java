package com.ssafy.gumid101.customercenter;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.ReportEntity;
import com.ssafy.gumid101.entity.UserEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, Long>{
	Optional<ReportEntity> findByUserReporterEntityAndReportCrewBoardSeq(UserEntity userEntity, Long reportCrewBoardSeq);
}
