package com.example.backend.service;

import com.example.backend.entity.Friend;
import com.example.backend.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    public List<Friend> getFriends(String name) {
        return friendRepository.findFriendsByFromUserOrToUserAndStatusTrue(name);
    }
    public List<Friend> requestFriends(String name) {
        return friendRepository.findFriendsByToUserAndStatusFalse(name);
    }

    public Friend deleteRequest(String fromName, String toName){
        return friendRepository.deleteFriendByFromUserAndToUser(fromName, toName);
    }
    public Friend requestFriend(String fromName, String toName) {
        return friendRepository.findFriendByFromUserAndToUser(fromName, toName);
    }
    public Friend saveFriend(Friend friend){
        return friendRepository.save(friend);
    }
}
