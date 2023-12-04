package com.example.dotabot1.model.dto.matchesdetails;

import lombok.Data;

@Data
public class Players {

    private String account_id;
    private int player_slot;


    public boolean playerIsRadiant(int player_slot) {
        return player_slot <= 4;
    }


}


