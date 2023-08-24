package com.example.backend.repository;

import com.example.backend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query(value = "SELECT ff.friendsId" +
            "            , ff.id, p.profile, u.user_name, u.user_addr3, u.user_status_message, u.online " +
            "           FROM (SELECT CASE " +
            "                           WHEN f.from_friend_user = :name" +
            "                           THEN f.to_friend_user" +
            "                           WHEN f.to_friend_user = :name" +
            "                           THEN f.from_friend_user" +
            "                        ELSE NULL " +
            "           END AS friendsId, " +
            "           f.status, " +
            "           f.id "+
            "    FROM FRIEND f " +
            "    WHERE f.to_friend_user = :name AND f.status = true " +
            ") AS ff " +
            "LEFT JOIN USER u ON ff.friendsId = u.user_id " +
            "LEFT JOIN PICTURE p ON u.user_id = p.user_id",
            nativeQuery = true)
    List<Map<String, Object>> findFriendsByFromUserOrToUserAndStatusTrue(@Param("name") String name);

    @Query(value =
            "SELECT ff.friendsId,ff.id, p.profile, u.user_name, u.user_addr3, u.user_status_message " +
                    "FROM ( " +
                    "    SELECT CASE " +
                    "           WHEN f.to_friend_user = :name THEN f.from_friend_user " +
                    "           ELSE NULL " +
                    "           END AS friendsId, " +
                    "           f.status, " +
                    "           f.id "+
                    "    FROM FRIEND f " +
                    "    WHERE f.to_friend_user = :name AND f.status = false " +
                    ") AS ff " +
                    "LEFT JOIN USER u ON ff.friendsId = u.user_id " +
                    "LEFT JOIN PICTURE p ON u.user_id = p.user_id",
            nativeQuery = true)
    List<Map<String, Object>> findFriendsByToUserAndStatusFalse(@Param("name") String name);




    Friend deleteFriendByFromUserAndToUser(String fromName, String toName);
    Friend findFriendByFromUserAndToUser(String fromName, String toName);

}