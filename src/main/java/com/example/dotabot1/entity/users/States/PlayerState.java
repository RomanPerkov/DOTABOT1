package com.example.dotabot1.entity.users.States;


/**
 * Класс пеерчисление описывающий состояние бота
 */
public enum PlayerState {
    DEFAULT(1),
    UPDATE_STEAM_ID(2),
    UPDATE_API_KEY(3);

    private final int code;

    PlayerState(int code) {
        this.code = code;
    }


}
