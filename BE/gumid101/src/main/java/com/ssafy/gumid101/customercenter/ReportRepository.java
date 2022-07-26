package com.ssafy.gumid101.customercenter;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.QuestionEntity;
import com.ssafy.gumid101.entity.ReportEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, Long>{

}
