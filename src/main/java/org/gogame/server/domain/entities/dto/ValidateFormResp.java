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
public class ValidateFormResp {

    // TODO można to dostować
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class QuestionVerdict {
        private Long id;
        private Integer score;
        private String explanation;
    }

    private List<QuestionVerdict> questionVerdicts;
}
