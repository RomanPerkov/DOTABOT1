//package com.example.dotabot1.entity.users;
//
//
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;
//
//import javax.persistence.*;
//
//@ToString
//@Entity
//@Getter
//@Setter
//@Table(name = "dota_now")
//@NoArgsConstructor
//public class DotaNow {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Integer id;
//
//    @Column(name = "play_time_forever")
//    private int playTimeForever;
//
//    @Column(name = "last_game")
//    private String lastGame;
//
//    @Column(name = "start_session")
//    private String startSession;
//
//    @Column(name = "end_session")
//    private String endSession;
//
//    @Column(name = "duration_session")
//    private String durationSession;
//
//    @Column(name = "state")
//    private int state;
//
//    public DotaNow(String startSession, int state) {
//        this.startSession = startSession;
//        this.state = state;
//    }
//
//}
