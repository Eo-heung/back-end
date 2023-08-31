package com.example.backend.service.impl;

import com.example.backend.entity.AppBoard;
import com.example.backend.entity.AppFixed;
import com.example.backend.repository.AppBoardRepository;
import com.example.backend.repository.AppFixedRepository;
import com.example.backend.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
    private final AppBoardRepository appBoardRepository;
    private final AppFixedRepository appFixedRepository;


    @Transactional
    @Override
    public AppBoard createApp(AppBoard appBoard) {
        AppBoard savedAppBoard = appBoardRepository.save(appBoard);

        AppFixed appFixed = new AppFixed();
        appFixed.setAppBoardId(savedAppBoard);
        appFixed.setAppFixedUser(savedAppBoard.getOwnerId());
        appFixed.setAppSort(AppFixed.AppSort.HOST);
        appFixed.setAppState(AppFixed.AppState.CONFIRM);

        appFixedRepository.save(appFixed);

        return savedAppBoard;
    }

}
