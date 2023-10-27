package com.example.dotabot1.services;

import com.example.dotabot1.dto.matchesdetails.Players;
import com.example.dotabot1.entity.users.User;
import com.example.dotabot1.repository.PlayerRepository;
import com.example.dotabot1.services.dotaapiservice.SteamApiService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GameMatchService {
    private final SteamApiService steamApiService;
    private final MessageGeneratorService messageGeneratorService;
    private final PlayerRepository playerRepository;
    ///private final OpenDotaApiService steamApiService;
    private static final Logger logger = LoggerFactory.getLogger(GameMatchService.class);


    private String to32bitAccountId(String bit) {
        long steamId64 = Long.parseLong(bit);
        long steamId32 = steamId64 - 76561197960265728L;
        return String.valueOf(steamId32);
    }


    public Mono<Boolean> didPlayerWin(String accountId, String matchId, Long chatId) {
        return steamApiService.getMatchDetails(matchId, playerRepository.findByChatId(chatId).getSteamApiKey())
                .flatMap(match -> {
                    System.out.println(match);
                    for (Players player : match.getResult().getPlayers()) {
                        System.out.println(player.getAccount_id());
                        System.out.println(accountId);
                        if (player.getAccount_id().equals(to32bitAccountId(accountId))) {
                            messageGeneratorService.playerMatchsStats(chatId,match);

                            return Mono.just(player.playerIsRadiant(player.getPlayer_slot()) == match.getResult().isRadiant_win());
                        }
                    }
                    return Mono.error(new IllegalArgumentException("Игрок с таким ID не найден в этом матче."));
                });
    }


    public Mono<Long> getIdMatchesList(Long chatId) {
        User user = playerRepository.findByChatId(chatId);
        if (user.getSteamApiKey() != null && user.getSteamId() != null) {
            return steamApiService.getPlayerMatchHistory(user.getSteamId(), user.getSteamApiKey())
                    .flatMap(requestHistory -> {
                        // Получаем ID последнего матча
                        Long lastMatchId =requestHistory.getResult().getMatches().get(0).getMatchId();
                        return Mono.just(lastMatchId);
                    });
        } else {
            return Mono.error(new IllegalStateException("Steam API Key or Steam ID is null."));
        }
    }

    public Mono<Boolean> checkLastMatchWin(Long chatId) {
        return getIdMatchesList(chatId)
                .flatMap(lastMatchId -> didPlayerWin(playerRepository.findByChatId(chatId).getSteamId(), lastMatchId.toString(), chatId));
    }













//    public void getIdMatchesList(Long chatId) {
//        User user = playerRepository.findByChatId(chatId);
//        if (user.getSteamApiKey() != null && user.getSteamId() != null) {
//            Optional.ofNullable(user)
//                    .ifPresent(
//                            u -> {
//                                Mono<RequestHistory> matchListMono = steamApiService.getPlayerMatchHistory(u.getSteamId(), u.getSteamApiKey());
//                                matchListMono.subscribe(request -> {
//                                            Optional.ofNullable(request)
//                                                    .map(RequestHistory::getResult)
//                                                    .map(Result::getMatches)
//                                                    .ifPresent(matches -> messageGeneratorService.matchIdMessage(chatId, String.valueOf(matches)));
//                                        },
//                                        error -> {
//                                            //обработка эксепшена
//                                            logger.error("Error getting id matches {}", chatId);
//                                        }
//                                );
//                            }
//
//                    );
//        } else {
//            messageGeneratorService.nullApiOrIdMessage(chatId);
//        }
//    }
}
