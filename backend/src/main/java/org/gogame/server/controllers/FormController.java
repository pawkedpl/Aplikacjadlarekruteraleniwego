package org.gogame.server.controllers;

import com.google.gson.Gson;
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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class FormController {

    private final FormService formService;
    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;
    private final Gson gson = new Gson();

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

        // asynchronicznie uruchamiamy walidację – niczego nie łapiemy
        formService.validateForm(newUuid, submitFormReq);

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
        var validatonResp = gson.fromJson(resultEntity.getJsonResult(), ValidateFormResp.class);
        return new ResponseEntity<>(validatonResp, HttpStatus.OK);
    }
}
