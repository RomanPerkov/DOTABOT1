package com.example.dotabot1.aspects;

import com.example.dotabot1.services.MessageGeneratorService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Класс аспект для обработки исключений методов
 */
@Aspect
@Component
@RequiredArgsConstructor
public class ExceptionInterceptorAspect {

    @Value("${dev.telegrmaId}")
    private Long devChatId;

   private final MessageGeneratorService messageGeneratorService;

    private static final Logger logger = LoggerFactory.getLogger(ExceptionInterceptorAspect.class);

    @AfterThrowing(pointcut = "execution(* com.example.dotabot1..*(..)) && !execution(* com.example.dotabot1.services.MessageGeneratorService.exceptionSendFromDeveloper(..))", throwing = "ex")   // обрабатываем все классы в проекте
    public void afterThrowingAdvice(JoinPoint joinPoint, Throwable ex) {
        // Логирование и другие операции
        logger.error("An exception has been thrown in {} : {}", joinPoint.getSignature().toShortString(), ex.getMessage());
      messageGeneratorService.exceptionSendFromDeveloper(devChatId,ex.getMessage());
    }
}
