package com.ssafy.gumid101.crew;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.RunRecordEntity;

public interface RunRecordRepository extends JpaRepository<RunRecordEntity, Long>  {

}
