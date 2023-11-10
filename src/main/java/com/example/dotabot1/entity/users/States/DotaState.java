package com.example.dotabot1.entity.users.States;

/**
 * Класс перечисление описывающий состояния стим аккаунта в игре или нет
 */

public enum DotaState {
    INGAME(1),
    NONGAME(2);

    private final int code;

    DotaState(int code) {
        this.code = code;
    }


}
