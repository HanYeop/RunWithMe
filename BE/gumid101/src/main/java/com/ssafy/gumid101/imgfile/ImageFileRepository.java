package com.ssafy.gumid101.imgfile;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.gumid101.entity.ImageFileEntity;

public interface ImageFileRepository extends JpaRepository<ImageFileEntity, Long> {

}
