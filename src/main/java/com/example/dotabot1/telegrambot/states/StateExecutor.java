package com.example.dotabot1.telegrambot.states;

import com.example.dotabot1.dto.player.Request;
import com.example.dotabot1.entity.users.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


/**
 * Класс создан для выполнения шаблонного кода , содержит метод execute который подписывается на моно и обрабатывает его ,
 */
@Service
@RequiredArgsConstructor
public class StateExecutor {

    private static final Logger logger = LoggerFactory.getLogger(StateExecutor.class);

    /**
     * Метод выполняет шаблонную операцию по извлечения сущности из БД и подписывается на  Mono переданный в параметры для обработки ответа
     * @param chatId айди чата в телеграм
     * @param messageText отправляемый текст в данном контексте
     * @param successAction функция для обработки успешного запроса
     * @param failureAction функция для обработки неудачного запроса
     * @param mono асинхронный запрос к API
     *  apply - метод в параметры которого передается функция, реализация которой в виде лямбд происходит в классах имплементирующих StateHandler и выполняющих логику
     * обработки асинхронного запросоа к API
     */



    public void execute(User user, Long chatId, String messageText, Mono<Request> mono, StateAction successAction, StateAction failureAction ) {
                 mono           // выполняет ассихронный запрос к API
                .subscribe(
                        request -> successAction.apply(user, messageText),  // обрабатывает успешный запрос
                        error -> failureAction.apply(user, messageText),        // обрабатывает неудачный запрос
                        () -> logger.info("Processing complete for chat ID: {}", chatId)  // логирует успешное завершение
                );
    }
}
