package com.example.dotabot1.entity.users;

import com.example.dotabot1.entity.users.States.DotaState;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "dota_stats")
public class DotaStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "total_games")
    private int totalGames;

    @Column(name = "total_wins")
    private int totalWins;

    @Column(name = "total_losses")
    private int totalLosses;

    @Column(name = "average_kills")
    private double averageKills;

    @Column(name = "average_deaths")
    private double averageDeaths;

    @Column(name = "average_assists")
    private double averageAssists;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private DotaState status;


    // Ссылка на Player для обратной связи
    @OneToOne(mappedBy = "dotaStatsId")
    private User player;
}
