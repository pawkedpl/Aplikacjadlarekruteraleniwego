package org.gogame.server.controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gogame.server.service.OpenRouterClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class GradeController {

    private final OpenRouterClient ai;

    @PostMapping("/grade")
    public ResponseEntity<?> grade(@RequestBody GradeReq req) {
        if (req == null || req.question == null || req.answer == null) {
            return ResponseEntity.badRequest().body("Brak 'question' lub 'answer'");
        }
        try {
            var out = ai.grade(req.question, req.answer).block();
            if (out == null) {
                return ResponseEntity.status(502).body("Brak odpowiedzi od AI");
            }
            return ResponseEntity.ok(new GradeResp(out.score(), out.feedback()));
        } catch (Exception e) {
            // przekażmy błąd od OpenAI do frontu (np. 401 gdy klucz zły / billing)
            return ResponseEntity.status(502).body("AI error: " + e.getMessage());
        }
    }

    // proste DTOs
    @Data public static class GradeReq {
        private String question;
        private String answer;
    }

    @Data public static class GradeResp {
        private float score;
        private String feedback;

        public GradeResp(float score, String feedback) {
            this.score = score;
            this.feedback = feedback;
        }
    }
}
