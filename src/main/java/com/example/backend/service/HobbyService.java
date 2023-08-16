package com.example.backend.service;

import com.example.backend.entity.Hobby;
import com.example.backend.entity.User;
import com.example.backend.repository.HobbyRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HobbyService {

    private final HobbyRepository hobbyRepository;

    public List<Hobby> getHobby() {
        return hobbyRepository.findAll();
    }

}
