package org.gogame.server.domain.entities.dto;

import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.Nullable;

public record TranscriptionRequest(MultipartFile audioFile, @Nullable String context) {}
