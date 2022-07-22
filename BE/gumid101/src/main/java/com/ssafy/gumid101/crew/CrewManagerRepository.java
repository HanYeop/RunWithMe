package com.ssafy.gumid101.crew;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.CrewEntity;

public interface CrewManagerRepository extends JpaRepository<CrewEntity, Long> {
	

}