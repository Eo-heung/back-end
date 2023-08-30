package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "SIREN")
public class Siren{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "singo_id")
    private Integer singoId;

    @Column(name = "singo_date")
    private LocalDateTime singoDate;

    @Column(name = "singoza_id")
    private String singozaId;

    @Column(name = "pisingoza_id")
    private String pisingozaId;

    @Column(name = "singo_categorycode")
    private String singoCategoryCode;

    @Column(name = "singo_content")
    private String singoContent;

    @Column(name = "singo_time")
    private LocalDateTime singoTime;

    @Column(name = "singo_msg")
    private String singoMsg;

    @Lob
    @Column(name = "singo_img1")
    private byte[] singoImg1;

    @Lob
    @Column(name = "singo_img2")
    private byte[] singoImg2;

    @Lob
    @Column(name = "singo_img3")
    private byte[] singoImg3;


    @PrePersist
    public void onPrePersist() {
        this.singoTime = LocalDateTime.now();
    }
}
