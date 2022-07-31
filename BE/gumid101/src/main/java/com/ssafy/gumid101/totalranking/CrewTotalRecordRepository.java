package com.ssafy.gumid101.totalranking;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.CrewTotalRecordEntity;

public interface CrewTotalRecordRepository extends JpaRepository<CrewTotalRecordEntity, Long>,CrewTotalRecordCustomRepository {


}
