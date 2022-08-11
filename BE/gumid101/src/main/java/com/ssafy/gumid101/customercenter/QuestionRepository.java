package com.ssafy.gumid101.customercenter;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.QuestionEntity;
import com.ssafy.gumid101.req.QuestionReqDto;

public interface QuestionRepository extends JpaRepository<QuestionEntity,Long>,CustomerCenterCustomRepository{




}
