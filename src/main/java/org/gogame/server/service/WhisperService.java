package org.gogame.server.service;


import org.gogame.server.domain.entities.dto.TranscriptionRequest;
import org.gogame.server.domain.entities.dto.TranscriptionResponse;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.stereotype.Service;

@Service
public class WhisperService {

    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

    public WhisperService(OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel) {
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
    }

    public TranscriptionResponse transcribe(TranscriptionRequest transcriptionRequest) {
        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(
            transcriptionRequest.audioFile().getResource(),
            OpenAiAudioTranscriptionOptions
                .builder()
                .prompt(transcriptionRequest.context())
                .build()
        );

        AudioTranscriptionResponse response = openAiAudioTranscriptionModel.call(prompt);
        return new TranscriptionResponse(response.getResult().getOutput());
    }
}