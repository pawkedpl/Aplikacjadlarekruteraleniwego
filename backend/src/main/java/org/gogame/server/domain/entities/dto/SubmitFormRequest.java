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
public class SubmitFormRequest {

    @Builder
    public record Response(String id, String question, String answer) {}

    private List<Response> responses;
}
