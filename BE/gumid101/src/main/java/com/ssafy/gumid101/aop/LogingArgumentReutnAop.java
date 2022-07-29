package com.ssafy.gumid101.aop;

import java.lang.reflect.Field;

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
		log.debug("[시작전] 컨트롤러: {} - 메서드 : {}", joinPoint.getTarget(), joinPoint.getSignature().toShortString());
		Object[] args = joinPoint.getArgs();

		for (int i = 0; i < args.length; i++) {
			java.lang.reflect.Field[] fields = joinPoint.getTarget().getClass().getFields();

			if (fields.length > 0) {
				for (int j = 0; j < fields.length; j++) {
					log.debug("{} : {}", fields[j].toString(), fields[j].getName());
				}
			} else {

				log.debug("{} : {}", args.getClass().getName(), args[i].toString());
			}
		}
	}

	@AfterReturning(value = "serviceCapturePoint()", returning = "returnObj")
	public void afterSuccessServiceAfter(JoinPoint joinPoint, Object returnObj) {
		log.debug("[정상적 반환] 컨트롤러: {} - 메서드 : {}", joinPoint.getTarget(), joinPoint.getSignature().toShortString());

		log.debug("value : {}", returnObj);

	}

	@AfterThrowing(value = "serviceCapturePoint()", throwing = "exception")
	public void afterExceptionServiceAfter(JoinPoint joinPoint, Object exception) {
		log.debug("[예외 발생] 컨트롤러: {} - 메서드 : {}", joinPoint.getTarget(), joinPoint.getSignature().toShortString());
		log.debug(exception.toString());
	}

}
