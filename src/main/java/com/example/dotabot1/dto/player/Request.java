package com.example.dotabot1.dto.player;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс ДТО описывающий игрока доты
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Request {
    private Response response;
}
