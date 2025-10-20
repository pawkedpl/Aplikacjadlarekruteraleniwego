package org.gogame.server.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Wartości są bindowane z application.yml -> prefix: openrouter
 * Przykład:
 * openrouter:
 *   api-key: sk-or-...
 *   base-url: https://openrouter.ai/api/v1
 *   model: openai/gpt-4o-mini
 *   referer: http://localhost
 *   title: Leniwy Rekruter
 */
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ConfigurationProperties(prefix = "openrouter")
public class ApiConfig {
    private String apiKey;   // openrouter.api-key
    private String baseUrl;  // openrouter.base-url
    private String model;    // openrouter.model
    private String referer;  // openrouter.referer (opcjonalne)
    private String title;    // openrouter.title (opcjonalne)
}
