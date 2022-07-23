package com.ssafy.gumid101.crew;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.gumid101.entity.UserCrewJoinEntity;

@Repository
public interface UserCrewJoinRepository extends JpaRepository<UserCrewJoinEntity, Long>{

}
