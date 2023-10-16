package com.example.dotabot1.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Класс аспект для обработки исключений методов
 */
@Aspect
@Component
public class ExceptionInterceptorAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionInterceptorAspect.class);

    @AfterThrowing(pointcut = "execution(* com.example.dotabot1..*(..))", throwing = "ex")   // обрабатываем все классы в проекте
    public void afterThrowingAdvice(JoinPoint joinPoint, Throwable ex) {
        // Логирование и другие операции
        logger.error("An exception has been thrown in {} : {}", joinPoint.getSignature().toShortString(), ex.getMessage());
    }
}
