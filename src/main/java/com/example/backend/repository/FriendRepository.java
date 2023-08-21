package com.example.backend.repository;

import com.example.backend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("SELECT f FROM Friend f WHERE (f.fromUser = :name OR f.toUser = :name) AND f.status = true")
    List<Friend> findFriendsByFromUserOrToUserAndStatusTrue(@Param("name") String name);

    @Query("SELECT f FROM Friend f WHERE (f.toUser = :name) AND f.status = false ")
    List<Friend> findFriendsByToUserAndStatusFalse(@Param("name") String name);

    Friend deleteFriendByFromUserAndToUser(String fromName, String toName);
    Friend findFriendByFromUserAndToUser(String fromName, String toName);

}
