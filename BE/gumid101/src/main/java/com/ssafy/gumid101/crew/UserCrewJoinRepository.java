package com.ssafy.gumid101.crew;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.UserCrewJoinEntity;

@Repository
public interface UserCrewJoinRepository extends JpaRepository<UserCrewJoinEntity, Long>{

	@Query(value = "DELETE FROM UserCrewJoinEntity ucj WHERE ucj.crewEntity = :crewSeq")
	int deleteAllBycrewSeq(@Param("crewSeq")CrewEntity crewSeq);

	@Modifying(flushAutomatically = true)
	//포인트는 비관적락을 걸고,벌크 연산을 통해 전체 환급시킨다.
	@Query(value="UPDATE UserEntity u SET u.point = u.point + COALESCE(:cost,0) "+
	"WHERE u IN (SELECT ucj.userEntity FROM UserCrewJoinEntity ucj INNER JOIN ucj.crewEntity c WHERE c = :crew) ")
	int pointRefunds(@Param("crew") CrewEntity crew,int cost);

}
