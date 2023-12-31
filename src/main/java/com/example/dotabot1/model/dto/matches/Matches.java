package com.example.dotabot1.model.dto.matches;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Matches {
    @JsonProperty("match_id")
    private long matchId;
    private Long start_time;

}
