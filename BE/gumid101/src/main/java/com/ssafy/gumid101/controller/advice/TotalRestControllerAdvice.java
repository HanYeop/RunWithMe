package com.ssafy.gumid101.controller.advice;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ssafy.gumid101.customexception.CrewNotFoundException;
import com.ssafy.gumid101.customexception.CrewPermissonDeniedException;
import com.ssafy.gumid101.customexception.DuplicateException;
import com.ssafy.gumid101.customexception.IllegalParameterException;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.customexception.PasswrodNotMatchException;
import com.ssafy.gumid101.customexception.RequestAlreadyProcessingException;
import com.ssafy.gumid101.customexception.ThirdPartyException;
import com.ssafy.gumid101.res.ResponseFrame;

@RestControllerAdvice
public class TotalRestControllerAdvice {

	@ExceptionHandler({NotFoundUserException.class})
	public ResponseEntity<?> userNofoundControll(NotFoundUserException nue) {

		return new ResponseEntity<>(ResponseFrame.of(false, nue.getMessage()), HttpStatus.OK);
	}

	@ExceptionHandler(CrewPermissonDeniedException.class)
	public ResponseEntity<?> crewPermisionDnieHandler(CrewPermissonDeniedException nue) {

		return new ResponseEntity<>(ResponseFrame.of(false, nue.getMessage()), HttpStatus.OK);
	}
	
	@ExceptionHandler(RequestAlreadyProcessingException.class)
	public ResponseEntity<?> duplicationRequestDenyHandler(RequestAlreadyProcessingException nue) {

		return new ResponseEntity<>(ResponseFrame.of(false, nue.getMessage()), HttpStatus.OK);
	}

	
	@ExceptionHandler(IllegalParameterException.class)
	public ResponseEntity<?> catchAllException(IllegalParameterException e) {
		ResponseFrame<String> responseFrame = new ResponseFrame<String>();

		responseFrame.setCount(0);
		responseFrame.setSuccess(false);
		responseFrame.setData(e.getMessage());

		return new ResponseEntity<>(responseFrame, HttpStatus.OK);
	}
	
	/**
	 * 우리 자체의 오류가 아니라 , S3를 사용하면서 난 오류이다.
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ThirdPartyException.class)
	public ResponseEntity<?> thirdParthExceptionHandle(ThirdPartyException e) {

		ResponseFrame<?> res = new ResponseFrame<>();

		res.setCount(0);
		res.setData(null);
		res.setSuccess(false);

		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity<?> duplicationExceptionHandle(DuplicateException de) {
		ResponseFrame<String> responseFrame = new ResponseFrame<String>();

		responseFrame.setCount(1);
		responseFrame.setSuccess(false);
		responseFrame.setData(de.getMessage());

		return new ResponseEntity<>(responseFrame, HttpStatus.OK);
	}


	

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<?> userSeqNotFoundHandler(UsernameNotFoundException e) {
		return new ResponseEntity<>(ResponseFrame.of(false, e.getMessage()), HttpStatus.OK);
	}

	@ExceptionHandler(CrewNotFoundException.class)
	public ResponseEntity<?> crewSeqNotFoundHandler(CrewNotFoundException e) {

		return new ResponseEntity<>(ResponseFrame.of(false, e.getMessage()), HttpStatus.OK);

	}

	@ExceptionHandler(PasswrodNotMatchException.class)
	public ResponseEntity<?> crewSeqNotFoundHandler(PasswrodNotMatchException e) {

		return new ResponseEntity<>(ResponseFrame.of(false, e.getMessage()), HttpStatus.OK);

	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> crewSeqNotFoundHandler(Exception e) {

		return new ResponseEntity<>(ResponseFrame.of(false,"처리되지 않은 에러 -"+ e.getMessage()), HttpStatus.OK);

	}
	
}
