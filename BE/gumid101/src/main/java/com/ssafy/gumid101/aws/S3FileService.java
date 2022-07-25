package com.ssafy.gumid101.aws;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.dto.ImageFileDto;

public interface S3FileService {
//	public InputStream getObject(String storedFileName) throws IOException;
	public ImageFileDto upload(MultipartFile multipartFile,String savedPath) throws Exception;
	public InputStream getObject(ImageFileDto imageDto) throws IOException;
}
