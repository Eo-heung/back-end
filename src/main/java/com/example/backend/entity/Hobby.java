package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "HOBBY")
public class Hobby {

    @Id
    @Column(name = "hobby_code")
    private int hobbyCode;

    @Column(name = "hobby_name")
    private String hobbyName;
}
