package com.ssafy.gumid101.aws;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.ssafy.gumid101.dto.ImageFileDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3FileServiceImpl implements S3FileService {
	private final AmazonS3Client amazonS3Client;
	
	@Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름
	
	/**
	 * 
	 * @param multipartFile 저장될 파일 
	 * @param savedPath 저장될 파일의 엣지까지의 디렉토리
	 * @return
	 * @throws IOException
	 */
	public ImageFileDto upload(MultipartFile multipartFile,String savedPath) throws Exception{
		
		
		
		String savedFileName = UUID.randomUUID().toString() ;
		
		long size = multipartFile.getSize(); // 파일 크기
		
		ObjectMetadata objectMetaData = new ObjectMetadata();
		objectMetaData.setContentType(multipartFile.getContentType());
		objectMetaData.setContentLength(size);
		
		// S3에 업로드
		amazonS3Client.putObject(
			new PutObjectRequest(bucket, savedPath + "/" + savedFileName, multipartFile.getInputStream(), objectMetaData)
				.withCannedAcl(CannedAccessControlList.PublicRead)
		);
		
		String imagePath = amazonS3Client.getUrl(bucket,savedPath+ "/" +savedFileName).toString(); // 접근가능한 URL 가져오기

		log.info("S3 버켓({}) <={}",bucket,imagePath);
		//사실상 savedpath 
		
		//유저는 /images/{imgseq}

		
		return ImageFileDto.builder().imgSavedName(savedFileName).imgSavedPath(savedPath).imgOriginalName(multipartFile.getOriginalFilename()).build();
    }
	
//	public InputStream getObject(String storedFileName) throws IOException {
//	      //  Content-Type : image/jpeg 
//        S3Object o = amazonS3Client.getObject(new GetObjectRequest(bucket, storedFileName));
//        //
//        
//        S3ObjectInputStream objectInputStream = ((S3Object) o).getObjectContent();
//        
//        InputStream is = (InputStream)objectInputStream;
//        
//       // byte[] bytes = IOUtils.toByteArray(objectInputStream);
// 
//        //String fileName = URLEncoder.encode(storedFileName, "UTF-8").replaceAll("\\+", "%20");
//
//       // HttpHeaders httpHeaders = new HttpHeaders();
//       // httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//       // httpHeaders.setContentLength(bytes.length);
//        //httpHeaders.setContentDispositionFormData("attachment", fileName);
// 
//        return is;
// 
	@Override
	public InputStream getObject(ImageFileDto imageDto) throws IOException {
        S3Object o = amazonS3Client.getObject(new GetObjectRequest(bucket, imageDto.getImgSavedPath() + "/" + imageDto.getImgSavedName()));
//        S3Object o = amazonS3Client.getObject(new GetObjectRequest(bucket, imageDto.getImgSavedName()));
        
        return o.getObjectContent();
//        S3ObjectInputStream objectInputStream = o.getObjectContent();
//        return IOUtils.toByteArray(objectInputStream);
    }

	
	
	
}
