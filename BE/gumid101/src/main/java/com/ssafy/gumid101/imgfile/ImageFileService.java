package com.ssafy.gumid101.imgfile;

import com.ssafy.gumid101.dto.ImageFileDto;

public interface ImageFileService {
	ImageFileDto getImageFile(long imageSeq) throws Exception;
}
