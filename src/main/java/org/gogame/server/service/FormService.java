package org.gogame.server.service;

import com.google.gson.Gson;
import org.gogame.server.config.ApiConfig;
import org.gogame.server.domain.entities.ResultEntity;
import org.gogame.server.domain.entities.dto.SubmitFormReq;
import org.gogame.server.domain.entities.dto.ValidateFormResp;
import org.gogame.server.repositories.ResultRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FormService {

    private final ApiConfig apiConfig;
    private final ResultRepository resultRepository;
    private final Gson gson;

    public FormService(ApiConfig apiConfig, ResultRepository resultRepository) {
        this.apiConfig = apiConfig;
        this.resultRepository = resultRepository;
        this.gson = new Gson();
    }

    @Async
    public void validateForm(UUID uuid, SubmitFormReq submitFormReq) throws InterruptedException {

        // todo @pawked zaimplementuj interakcję z czatem tutaj
        {
            apiConfig.getApiKey();
            Thread.sleep(5000);

            var response = ValidateFormResp.builder()
                .questionVerdicts(List.of(
                    ValidateFormResp.QuestionVerdict
                        .builder()
                        .id(110L)
                        .score(4)
                        .explanation("The answer meets all the criteria.")
                        .build()
                ))
                .build();

            // workaround żeby wepchnąć ocenę do tablicy SQL
            resultRepository.save(
                ResultEntity.builder()
                    .uuid(uuid)
                    .jsonResult(gson.toJson(response))
                .build());
        }
    }

}
