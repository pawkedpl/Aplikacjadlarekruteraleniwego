package org.gogame.server.domain.entities.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record SubmitFormResponse(UUID uuid) {}
