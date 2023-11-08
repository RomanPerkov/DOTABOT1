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

    @Column(name = "start_session")
    Long startsession;



    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private DotaState status;


    // Ссылка на Player для обратной связи
    @OneToOne(mappedBy = "dotaStatsId")
    private User player;
}
