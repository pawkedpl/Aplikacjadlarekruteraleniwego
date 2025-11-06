package org.gogame.server.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.gogame.server.domain.entities.ResultEntity;
import org.gogame.server.domain.entities.dto.SubmitFormReq;
import org.gogame.server.domain.entities.dto.ValidateFormResp;
import org.gogame.server.repositories.ResultRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;   // <— brakujący import
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FormService {

    private final ResultRepository resultRepository;
    private final OpenRouterClient ai;           // <— wstrzykujemy klienta AI
    private final Gson gson = new Gson();

    @Async
    public void validateForm(UUID uuid, SubmitFormReq submitFormReq) {
        try {
            List<ValidateFormResp.QuestionVerdict> verdicts = new ArrayList<>();

            if (submitFormReq.getResponses() != null) {
                for (SubmitFormReq.Response r : submitFormReq.getResponses()) {
                    var question = r.getQuestion();
                    var answer   = r.getAnswer();


                    var out = ai.grade(question, answer).block();



                    float score = 0f;
                    String feedback = "No feedback";

                    if (out != null) {

                        score = out.score();
                        feedback = out.feedback();

                    }

                    Long id = null;
                    try { id = Long.valueOf(r.getId()); } catch (Exception ignore) {}

                    verdicts.add(ValidateFormResp.QuestionVerdict.builder()
                            .id(id)
                            .score(Math.round(score))
                            .explanation(feedback)
                            .build());
                }
            }

            var response = ValidateFormResp.builder()
                    .questionVerdicts(verdicts)
                    .build();

            resultRepository.save(ResultEntity.builder()
                    .uuid(uuid)
                    .jsonResult(gson.toJson(response))
                    .build());

        } catch (Exception e) {
            var error = ValidateFormResp.builder().questionVerdicts(List.of(
                    ValidateFormResp.QuestionVerdict.builder()
                            .id(null)
                            .score(0)
                            .explanation("AI error: " + e.getMessage())
                            .build()
            )).build();

            resultRepository.save(ResultEntity.builder()
                    .uuid(uuid)
                    .jsonResult(gson.toJson(error))
                    .build());
        }
    }
}
