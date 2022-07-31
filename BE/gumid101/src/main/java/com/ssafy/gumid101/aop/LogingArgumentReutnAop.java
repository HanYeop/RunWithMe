package com.ssafy.gumid101.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LogingArgumentReutnAop {
//인자보다는 파라메터긴 한데...
	// 컨트롤러는 인터셉터 쓴다네...

	@Pointcut("execution(* com.ssafy.gumid101..*Service+.*(..))")
	public void serviceCapturePoint() {
	}

	@Before(value = "serviceCapturePoint()")
	public void beforeServiceMethode(JoinPoint joinPoint) {
		System.out.println("kkk");
		log.debug("[시작전] 서비스: {} - 메서드 : {}", joinPoint.getTarget(), joinPoint.getSignature().toShortString());



	}

	@AfterReturning(value = "serviceCapturePoint()", returning = "returnObj")
	public void afterSuccessServiceAfter(JoinPoint joinPoint, Object returnObj) {
		log.debug("[정상적 반환] 서비스: {} - 메서드 : {}", joinPoint.getTarget(), joinPoint.getSignature().toShortString());

		log.debug("value : {}", returnObj);

	}

	@AfterThrowing(value = "serviceCapturePoint()", throwing = "exception")
	public void afterExceptionServiceAfter(JoinPoint joinPoint, Object exception) {
		log.debug("[예외 발생] 서비스: {} - 메서드 : {}", joinPoint.getTarget(), joinPoint.getSignature().toShortString());
		log.debug(exception.toString());
	}

}
