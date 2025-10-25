package org.gogame.server.domain.entities.dto;

import lombok.Builder;
import org.gogame.server.domain.entities.QuestionEntity;

import java.util.List;

@Builder
public record GetFormResponse(List<QuestionEntity> questions) {}
