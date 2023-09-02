package com.example.backend.repository;
import com.example.backend.entity.ChattingRoomName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChattingRoomNameRepository extends JpaRepository<ChattingRoomName, Integer> {

    // roomName을 이용해서 특정 ChattingRoomName을 검색하는 메서드
    Optional<ChattingRoomName> findByRoomName(String roomName);
}
