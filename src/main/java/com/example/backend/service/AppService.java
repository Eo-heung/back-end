package com.example.backend.service;

import com.example.backend.entity.AppBoard;
import com.example.backend.entity.User;

import java.time.LocalDateTime;

public interface AppService {
    AppBoard createApp(int moimId, AppBoard appBoard, User loginUser);



    boolean hasOverlappingAppointments(User user, LocalDateTime appStart, LocalDateTime appEnd);
}

