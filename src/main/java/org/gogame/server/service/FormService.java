package org.gogame.server.service;

import org.gogame.server.config.ApiConfig;
import org.gogame.server.domain.entities.ResultEntity;
import org.gogame.server.domain.entities.dto.SubmitFormReq;
import org.gogame.server.repositories.ResultRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FormService {

    private final ApiConfig apiConfig;
    private final ResultRepository resultRepository;

    public FormService(ApiConfig apiConfig, ResultRepository resultRepository) {
        this.apiConfig = apiConfig;
        this.resultRepository = resultRepository;
    }

    @Async
    public void validateForm(UUID uuid, SubmitFormReq submitFormReq) throws InterruptedException {

        // todo @pawked zaimplementuj interakcję z czatem tutaj
        {
            apiConfig.getApiKey();
            Thread.sleep(5000);

            resultRepository.save(
                ResultEntity.builder()
                    .uuid(uuid)
                    .jsonResult("{ \"status\": \"validated\" }")
                    .build());
        }
    }

}
