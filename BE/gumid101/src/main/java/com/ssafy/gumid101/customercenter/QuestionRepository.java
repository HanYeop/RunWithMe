package com.ssafy.gumid101.customercenter;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.QuestionEntity;

public interface QuestionRepository extends JpaRepository<QuestionEntity,Long>{

}
