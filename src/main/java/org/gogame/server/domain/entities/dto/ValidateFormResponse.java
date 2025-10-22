package org.gogame.server.domain.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateFormResponse {

    // TODO można to dostosować
    @Builder
    public record QuestionVerdict(Long id, Integer score, String explanation) {}

    private List<QuestionVerdict> questionVerdicts;
}
