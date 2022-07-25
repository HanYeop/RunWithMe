package com.ssafy.gumid101.imgfile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.gumid101.aws.S3FileService;
import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.res.ResponseFrame;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@Controller
@RequestMapping("/images")
@Slf4j
public class ImageController {
	

	private final ImageFileService imgService;
	private final S3FileService s3FileService;
	@GetMapping("/{image-seq}")
	public void getImage(@PathVariable("image-seq") Long imageSeq, @ApiIgnore HttpServletResponse response) throws Exception {
		
		ImageFileDto imageFileDto =  imgService.getImageFile(imageSeq);
		if(imageFileDto != null) {
//			String realFilePath = imageFileDto.getImgSavedPath() +"/"+ imageFileDto.getImgSavedName();
//			InputStream imgInputStream = s3FileService.getObject(realFilePath);
			InputStream imgInputStream = s3FileService.getObject(imageFileDto);
			
			
			BufferedInputStream bis = new BufferedInputStream(imgInputStream);
			BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
			
			byte[] buffer = new byte[1024 * 2];
			
			int read = 0 ;
			
			while((read = bis.read(buffer)) != -1) {
				bos.write(buffer);
			}
			
			bos.flush();
			response.flushBuffer();
			
			bis.close();
			bos.close();
		}
		else {
		}
//		ResponseFrame<byte[]> responseFrame = new ResponseFrame<>();
//		if (imageFileDto != null) {
//			responseFrame.setMsg("제발 돼라.");
//			responseFrame.setCount(1);
//			responseFrame.setData(s3FileService.getObject(imageFileDto));
//			responseFrame.setSuccess(true);
//		}
//		return new ResponseEntity<>(responseFrame, HttpStatus.OK);		
//		
	}
}
