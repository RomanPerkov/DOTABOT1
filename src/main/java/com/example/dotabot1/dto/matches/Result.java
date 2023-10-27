package com.example.dotabot1.dto.matches;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
public class Result {
    private int status;
    @JsonProperty("num_Results")
    private int numResults;
    private int totalResults;
    private int resultsRemaining;
    private List<Matches> matches;
}
