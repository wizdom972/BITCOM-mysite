package mysite.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class MeasureExecutionTimeAspect {
	@Around("execution(* *.repository.*.*(..)) || execution(* *.service.*.*(..)) || execution(* *.controller.*.*(..))")
	public Object adviceAround(ProceedingJoinPoint pjp) throws Throwable {
		/* before */
		StopWatch sw = new StopWatch();
		sw.start();

		Object result = pjp.proceed();

		/* after */
		sw.stop();
		long totalTime = sw.getTotalTimeMillis();
		String className = pjp.getTarget().getClass().getName();
		String methodName = pjp.getSignature().getName();
		String taskName = className + "." + methodName;

		log.info("[Excution Time][" + taskName + "] " + totalTime + " millis");

		return result;
	}
}
