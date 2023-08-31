package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "APP_FIXED")
public class AppFixed {
    @Id
    @Column(name = "app_fixed_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appFixedId; //확정된 약속 Id

    @ManyToOne
    @JoinColumn(name = "app_board_id")
    private AppBoard appBoardId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User appFixedUser; //약속 확정된 유저

    @Enumerated(EnumType.STRING)
    @Column(name = "app_sort")
    private AppSort appSort;   // HOST,GUEST

    @Enumerated(EnumType.STRING)
    @Column(name = "app_state")
    private AppState appState; //확정 CONFIRM, 거절 REJECT, 취소 CANCEL, 기달 WAIT


    public enum AppSort {
        HOST, GUEST
    }

    public enum AppState {
        CONFIRM, REJECT, CANCEL, WAIT
    }


}
