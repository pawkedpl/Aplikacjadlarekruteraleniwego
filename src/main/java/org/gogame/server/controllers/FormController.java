package org.gogame.server.controllers;

import lombok.RequiredArgsConstructor;
import org.gogame.server.domain.entities.ResultEntity;
import org.gogame.server.domain.entities.dto.GetFormResp;
import org.gogame.server.domain.entities.dto.SubmitFormReq;
import org.gogame.server.domain.entities.dto.SubmitFormResp;
import org.gogame.server.domain.entities.dto.ValidateFormResp;
import org.gogame.server.repositories.QuestionRepository;
import org.gogame.server.repositories.ResultRepository;
import org.gogame.server.service.FormService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class FormController {

    private final FormService formService;
    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;

    @GetMapping("/getForm")
    public ResponseEntity<GetFormResp> getForm() {
        var questions = questionRepository.findAll();

        if (questions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
            GetFormResp.builder()
                .questions(questions)
                .build(),
            HttpStatus.OK);
    }

    @PutMapping("/submitForm")
    public ResponseEntity<SubmitFormResp> submitForm(@RequestBody SubmitFormReq submitFormReq) {
        var newUuid = UUID.randomUUID();
        resultRepository.save(ResultEntity.builder()
            .uuid(newUuid)
            .jsonResult(null)
            .build());

        try {
            formService.validateForm(newUuid, submitFormReq);
        } catch (InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        var response = SubmitFormResp.builder()
            .uuid(newUuid)
            .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/checkResults/{uuid}")
    public ResponseEntity<ValidateFormResp> checkResults(@PathVariable UUID uuid) {
        var resultEntityOpt = resultRepository.findByUuid(uuid);

        if (resultEntityOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var resultEntity = resultEntityOpt.get();

        if (resultEntity.getJsonResult() == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // mock - do wywalenia
        {
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

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
