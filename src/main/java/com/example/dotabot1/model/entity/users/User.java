package com.example.dotabot1.model.entity.users;


import com.example.dotabot1.model.entity.users.States.PlayerState;
import jakarta.persistence.*;
import lombok.*;


/**
 * Класс описывает сущность в БД
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    // этот уникальный идентификатор пользователя в стим
    @Column(name = "user_number_in_steam")
    private String steamId;

    //это ключ API который пользователь сервиса будет вводить что бы он использовался для отправки запросов на dotaAPI
    @Column(name = "steam_api_key")
    private String steamApiKey;

    // id чата в телеграмме
    @Column(name = "chat_id")
    private Long chatId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="dota_stats_id")
    private DotaStats dotaStatsId;


    // состояние бота для пользователя
    @Enumerated(EnumType.STRING)   //храним энум в виде стринга
    private PlayerState state;
}