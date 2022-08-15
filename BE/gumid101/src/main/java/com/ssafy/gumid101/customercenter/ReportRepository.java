package com.ssafy.gumid101.customercenter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.gumid101.entity.ReportEntity;
import com.ssafy.gumid101.entity.UserEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, Long>,CustomerCenterCustomRepository{
	Optional<ReportEntity> findByUserReporterEntityAndReportCrewBoardSeq(UserEntity userEntity, Long reportCrewBoardSeq);

	
	@Query("SELECT new map(r.reportStatus,COUNT(r)) FROM ReportEntity r GROUP BY (r.reportStatus)")
	List<Map<String,Object>> getReportStateCountThoughtGroupBy();
	
	
}
