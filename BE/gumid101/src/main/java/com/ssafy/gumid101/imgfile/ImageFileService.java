package com.ssafy.gumid101.imgfile;

import com.ssafy.gumid101.dto.ImageFileDto;

public interface ImageFileService {
	ImageFileDto getImageFile(Long imageSeq) throws Exception;
}
