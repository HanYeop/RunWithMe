package com.ssafy.gumid101.aws;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface S3FileService {
	public InputStream getObject(String storedFileName) throws IOException;
	public String upload(MultipartFile multipartFile,String savedPath) throws Exception;
}
