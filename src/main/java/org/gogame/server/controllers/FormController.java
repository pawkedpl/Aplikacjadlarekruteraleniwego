package org.gogame.server.controllers;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.gogame.server.domain.entities.ResultEntity;
import org.gogame.server.domain.entities.dto.GetFormResponse;
import org.gogame.server.domain.entities.dto.SubmitFormRequest;
import org.gogame.server.domain.entities.dto.SubmitFormResponse;
import org.gogame.server.domain.entities.dto.ValidateFormResponse;
import org.gogame.server.repositories.QuestionRepository;
import org.gogame.server.repositories.ResultRepository;
import org.gogame.server.service.FormService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class FormController {

    private final FormService formService;

    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;

    private final Gson gson = new Gson();

    @GetMapping("/getForm")
    public ResponseEntity<GetFormResponse> getForm() {
        var questions = questionRepository.findAll();

        if (questions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
            GetFormResponse.builder()
                .questions(questions)
                .build(),
            HttpStatus.OK);
    }

    @PutMapping("/submitForm")
    public ResponseEntity<SubmitFormResponse> submitForm(@RequestBody SubmitFormRequest submitFormRequest) {
        var newUuid = UUID.randomUUID();
        resultRepository.save(ResultEntity.builder()
            .uuid(newUuid)
            .jsonResult(null)
            .build());

        try {
            formService.validateForm(newUuid, submitFormRequest);
        } catch (InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        var response = SubmitFormResponse.builder()
            .uuid(newUuid)
            .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/checkResults/{uuid}")
    public ResponseEntity<ValidateFormResponse> checkResults(@PathVariable UUID uuid) {
        var resultEntityOpt = resultRepository.findByUuid(uuid);

        if (resultEntityOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var resultEntity = resultEntityOpt.get();

        if (resultEntity.getJsonResult() == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        var validatonResp = gson.fromJson(resultEntity.getJsonResult(), ValidateFormResponse.class);
        return new ResponseEntity<>(validatonResp, HttpStatus.OK);
    }
}
