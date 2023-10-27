package com.example.dotabot1.dto.matchesdetails;

import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class Result {

    // @JsonProperty("match_id")
    private long match_id;
    //JsonProperty("radiant_win")
    private boolean radiant_win;
    private List<Players> players;
    private long duration;
    // @JsonProperty("start_time")
    private long start_time;

    public static String convertUnixToReadable(long unixTimestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = Instant.ofEpochSecond(unixTimestamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return formatter.format(dateTime);
    }
    public static String convertDuration(long unixTimestamp) {
        Duration duration = Duration.ofSeconds(unixTimestamp);
        return String.format("%d:%02d:%02d",
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart());

    }
}