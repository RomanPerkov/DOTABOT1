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


//    /**
//     * Конвертер юникс времени в нормальное
//     * @param unixTimestamp временная точка в юникс времени
//     * @return
//     */
//    public static String convertUnixToReadable(long unixTimestamp) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        Instant instant = Instant.ofEpochSecond(unixTimestamp);
//        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
//        return formatter.format(dateTime);
//    }
//
//    /**
//     * Конвертер юникс длительности в нормальную
//     * @param unixTimestamp продолжительность матча в юникс формате
//     * @return
//     */
//    public static String convertDuration(long unixTimestamp) {
//        Duration duration = Duration.ofSeconds(unixTimestamp);
//        return String.format("%d:%02d:%02d",
//                duration.toHours(),
//                duration.toMinutesPart(),
//                duration.toSecondsPart());
//
//    }
}