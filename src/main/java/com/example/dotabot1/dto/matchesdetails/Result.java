package com.example.dotabot1.dto.matchesdetails;

import lombok.Data;

import java.util.List;

@Data
public class Result {

    private Long match_id;
    private boolean radiant_win;
    private List<Players> players;
    private long duration;
    private long start_time;


}