package com.ssafy.gumid101.recommend;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.TrackBoardEntity;

public interface RecommendRepository extends JpaRepository<TrackBoardEntity, Long>{

}
