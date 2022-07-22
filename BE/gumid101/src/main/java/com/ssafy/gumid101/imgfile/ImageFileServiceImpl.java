package com.ssafy.gumid101.imgfile;

import org.springframework.stereotype.Service;

import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.entity.ImageFileEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageFileServiceImpl implements ImageFileService {

	private final ImageFileRepository imgFileRepo;
	
	@Override
	public ImageFileDto getImageFile(long imageSeq) throws Exception {
		
		ImageFileEntity imgEntity =  imgFileRepo.findById(imageSeq).orElse(null);
		
		if(imgEntity != null) {
			
			return ImageFileDto.of(imgEntity);
		}
		
		return null;
	}

}
