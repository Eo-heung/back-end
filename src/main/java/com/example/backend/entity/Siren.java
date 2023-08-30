package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "SIREN")
public class Siren {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "singo_id")
    private Integer singoId;

    @Column(name = "singo_date")
    private String singoDate;

    @Column(name = "singoza_id")
    private String singozaId;

    @Column(name = "pisingoza_id")
    private String pisingozaId;

    @Column(name = "singo_categorycode")
    private String singoCategoryCode;

    @Column(name = "singo_content")
    private String singoContent;

    @Column(name = "singo_time")
    private String singoTime;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss");
        this.singoTime = LocalDateTime.now().format(formatter);
    }
}