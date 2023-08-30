package com.example.backend.service;

import com.example.backend.dto.SirenDTO;
import com.example.backend.entity.Siren;
import com.example.backend.repository.SirenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class SirenService {

    private final SirenRepository sirenRepository;  // final 키워드 사용

    @Autowired

    public SirenService(SirenRepository sirenRepository) {
        this.sirenRepository = sirenRepository;
    }

    public Siren createSirenReport(SirenDTO sirenDTO) {
        Siren siren = convertDtoToEntity(sirenDTO);
        return sirenRepository.save(siren);
    }

    private Siren convertDtoToEntity(SirenDTO sirenDTO) {

        if (sirenDTO == null) {
            return null;
        }

        Siren siren = new Siren();

        siren.setSingoDate(sirenDTO.getSingoDate());
        siren.setSingozaId(sirenDTO.getSingozaId());
        siren.setPisingozaId(sirenDTO.getPisingozaId());
        siren.setSingoCategoryCode(sirenDTO.getSingoCategoryCode());
        siren.setSingoContent(sirenDTO.getSingoContent());
        siren.setSingoMsg(sirenDTO.getSingoMsg().toString());
        siren.setSingoImg1(sirenDTO.getSingoImg1());
        siren.setSingoImg2(sirenDTO.getSingoImg2());
        siren.setSingoImg3(sirenDTO.getSingoImg3());

        return siren;
    }
}
