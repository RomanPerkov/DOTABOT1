package com.example.dotabot1.aspects;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Класс логгер использует Spring AOP для перехвата и логгирования
 */
@Aspect
@Component
public class LoggerInterceptorAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggerInterceptorAspect.class);


    @Around("execution(* com.example.dotabot1..*(..))")
    public Object logMethodArguments(ProceedingJoinPoint joinPoint) throws Throwable {
        // логирование перед выполнением метода
        logger.info("Entering Method Signature" + joinPoint.getSignature().toShortString());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            logger.info("Argument: " + arg);
        }

        // выполнение оригинального метода
        Object result = joinPoint.proceed();

        // логирование после выполнения метода
        logger.info("Method "+joinPoint.getSignature().toShortString()+"returned: " + result);

        return result;
    }
}