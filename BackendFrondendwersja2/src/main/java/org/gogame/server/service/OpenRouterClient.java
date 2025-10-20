package org.gogame.server.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.gogame.server.config.ApiConfig;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OpenRouterClient {

    private final ApiConfig cfg;

    private WebClient client() {
        return WebClient.builder()
                .baseUrl(cfg.getBaseUrl()) // << https://api.openai.com
                .defaultHeader("Authorization", "Bearer " + cfg.getApiKey())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<GradeResult> grade(String question, String answer) {
        String body = """
        {
          "model": "%s",
          "messages": [
            {"role":"system","content":"Jesteś surowym egzaminatorem IT. Oceń odpowiedź w skali 0–5 (połówki dozwolone). Zwróć JSON: {\\"score\\": number, \\"feedback\\": string}. Oceniaj merytorykę, jasność i kompletność."},
            {"role":"user","content":"PYTANIE:\\n%s\\n\\nODPOWIEDŹ:\\n%s\\n\\nZwróć JSON jak opisano."}
          ],
          "temperature": 0,
          "response_format": { "type": "json_object" }
        }
        """.formatted(cfg.getModel(), esc(question), esc(answer));

        return client().post()
                .uri("/v1/chat/completions") // << ważne: /v1/chat/completions
                .bodyValue(body)
                .retrieve()
                .onStatus(s -> s.is4xxClientError() || s.is5xxServerError(),
                        resp -> resp.bodyToMono(String.class)
                                .map(msg -> new RuntimeException("AI HTTP " + resp.statusCode() + ": " + msg)))
                .bodyToMono(String.class)
                .map(resp -> {
                    String json = JsonParser.parseString(resp).getAsJsonObject()
                            .getAsJsonArray("choices").get(0).getAsJsonObject()
                            .getAsJsonObject("message")
                            .get("content").getAsString();

                    try {
                        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
                        double s = obj.get("score").getAsDouble();
                        String fb = obj.has("feedback") ? obj.get("feedback").getAsString() : "";
                        return new GradeResult((float) clamp(s, 0, 5), fb);
                    } catch (Exception e) {
                        float s = 0f;
                        try { s = Float.parseFloat(json.replace(',', '.').replaceAll("[^0-9.]", "")); } catch (Exception ignore) {}
                        return new GradeResult((float) clamp(s, 0, 5), json);
                    }
                });
    }

    private static String esc(String s){ return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n"); }
    private static double clamp(double v,double lo,double hi){ return Math.max(lo, Math.min(hi, v)); }

    public record GradeResult(float score, String feedback) {}
}
