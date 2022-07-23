package com.ssafy.gumid101.dto;

import java.io.Serializable;

import com.ssafy.gumid101.entity.ImageFileEntity;

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

    private Long imgSeq;

    private String imgOriginalName;
    
    private String imgSavedName;
    
    private String imgSavedPath;
    
    public static ImageFileDto of(ImageFileEntity imageFile) {
        return new ImageFileDtoBuilder()
                .imgSeq(imageFile.getImgSeq())
                .imgOriginalName(imageFile.getImgOriginalName())
                .imgSavedName(imageFile.getImgSavedName())
                .imgSavedPath(imageFile.getImgSavedPath())
                .build();
    }
}