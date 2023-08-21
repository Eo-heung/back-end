package com.example.backend.service;

import com.example.backend.entity.Friend;
import com.example.backend.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;


    @Transactional
    public List<Map<String, String>> getFriends(String name) {
        return friendRepository.findFriendsByFromUserOrToUserAndStatusTrue(name);
    }
    @Transactional
    public List<Map<String, String>> requestFriends(String name) {
        return friendRepository.findFriendsByToUserAndStatusFalse(name);
    }
    @Transactional
    public Friend deleteRequest(String fromName, String toName){
        return friendRepository.deleteFriendByFromUserAndToUser(fromName, toName);
    }
    @Transactional
    public Friend requestFriend(String fromName, String toName) {
        return friendRepository.findFriendByFromUserAndToUser(fromName, toName);
    }
    @Transactional
    public Friend saveFriend(Friend friend){
        return friendRepository.save(friend);
    }
}