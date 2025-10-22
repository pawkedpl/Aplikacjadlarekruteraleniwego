package org.gogame.server.controllers;

import lombok.RequiredArgsConstructor;
import org.gogame.server.domain.entities.dto.TranscriptionRequest;
import org.gogame.server.domain.entities.dto.TranscriptionResponse;
import org.gogame.server.service.WhisperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class WhisperController {

    private final WhisperService whisperService;

    @PostMapping("/transcribe")
    ResponseEntity<TranscriptionResponse> transcribe(
        @RequestParam("audioFile") MultipartFile audioFile,
        @RequestParam("context") String context
    ) {
        TranscriptionRequest transcriptionRequest = new TranscriptionRequest(audioFile, context);
        TranscriptionResponse response = whisperService.transcribe(transcriptionRequest);
        return ResponseEntity.ok(response);
    }

}
