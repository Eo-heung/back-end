package com.example.backend.repository;

import com.example.backend.entity.Moim;
import com.example.backend.entity.MoimPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MoimPictureRepository extends JpaRepository<MoimPicture, Integer> {

    Optional<MoimPicture> findByMoimId(Moim moim);

    @Query("SELECT mp FROM MoimPicture mp WHERE mp.moimId = :moim")
    MoimPicture findByMoim(@Param("moim") Moim moim);


}
