package com.ssafy.gumid101.recommend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.TrackBoardEntity;

public interface RecommendRepository extends JpaRepository<TrackBoardEntity, Long>{
	List<TrackBoardEntity> findByRunRecordEntity_RunRecordLatBetweenAndRunRecordEntity_RunRecordLngBetween(Double lowerLat, Double upperLat, Double leftLng, Double rightLng);
}
