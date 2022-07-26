package com.ssafy.gumid101.dto;

import java.io.Serializable;

import com.ssafy.gumid101.entity.ImageFileEntity;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 직렬화 기능을 가진 User클래스
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageFileDto implements Serializable {

	@ApiParam(value = "이미지 번호")
    private Long imgSeq;

	@ApiParam(value = "이미지 원래 이름")
    private String imgOriginalName;
    
	@ApiParam(value = "S3상 저장된 이름")
    private String imgSavedName;
    
	@ApiParam(value = "S3상 저장 경로")
    private String imgSavedPath;
    
    public static ImageFileDto of(ImageFileEntity imageFile) {
    	if(imageFile==null)
    		return null;
        return new ImageFileDtoBuilder()
                .imgSeq(imageFile.getImgSeq())
                .imgOriginalName(imageFile.getImgOriginalName())
                .imgSavedName(imageFile.getImgSavedName())
                .imgSavedPath(imageFile.getImgSavedPath())
                .build();
    }
}