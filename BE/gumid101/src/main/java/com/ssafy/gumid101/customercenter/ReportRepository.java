package com.ssafy.gumid101.customercenter;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.crew.manager.CrewManagerCustomRepository;
import com.ssafy.gumid101.entity.ReportEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.req.ReportSelectReqDto;

public interface ReportRepository extends JpaRepository<ReportEntity, Long>,CustomerCenterCustomRepository{
	Optional<ReportEntity> findByUserReporterEntityAndReportCrewBoardSeq(UserEntity userEntity, Long reportCrewBoardSeq);

	
}
