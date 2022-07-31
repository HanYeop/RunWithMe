package com.ssafy.gumid101.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ssafy.gumid101.customexception.CrewNotFoundException;
import com.ssafy.gumid101.customexception.CrewPermissonDeniedException;
import com.ssafy.gumid101.customexception.DuplicateException;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.customexception.PasswrodNotMatchException;
import com.ssafy.gumid101.customexception.ThirdPartyException;
import com.ssafy.gumid101.res.ResponseFrame;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

	@ExceptionHandler(NotFoundUserException.class)
	public ResponseEntity<?> userNofoundControll(NotFoundUserException nue) {

		return new ResponseEntity<>(ResponseFrame.of(false, nue.getMessage()), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(CrewPermissonDeniedException.class)
	public ResponseEntity<?> crewPermisionDnieHandler(NotFoundUserException nue) {

		return new ResponseEntity<>(ResponseFrame.of(false, nue.getMessage()), HttpStatus.FORBIDDEN);
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

		return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity<?> duplicationExceptionHandle(DuplicateException de) {
		ResponseFrame<String> responseFrame = new ResponseFrame<String>();

		responseFrame.setCount(1);
		responseFrame.setSuccess(false);
		responseFrame.setData(de.getMessage());

		return new ResponseEntity<>(responseFrame, HttpStatus.CONFLICT);
	}


	

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<?> userSeqNotFoundHandler(UsernameNotFoundException e) {
		return new ResponseEntity<>(ResponseFrame.of(false, e.getMessage()), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(CrewNotFoundException.class)
	public ResponseEntity<?> crewSeqNotFoundHandler(UsernameNotFoundException e) {

		return new ResponseEntity<>(ResponseFrame.of(false, e.getMessage()), HttpStatus.FORBIDDEN);

	}

	@ExceptionHandler(PasswrodNotMatchException.class)
	public ResponseEntity<?> crewSeqNotFoundHandler(PasswrodNotMatchException e) {

		return new ResponseEntity<>(ResponseFrame.of(false, e.getMessage()), HttpStatus.FORBIDDEN);

	}
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> catchAllException(Exception e) {
		ResponseFrame<String> responseFrame = new ResponseFrame<String>();

		responseFrame.setCount(0);
		responseFrame.setSuccess(false);
		responseFrame.setData(e.getMessage());

		return new ResponseEntity<>(responseFrame, HttpStatus.BAD_REQUEST);
	}
	
}
