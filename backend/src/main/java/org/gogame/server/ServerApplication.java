package org.gogame.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gogame.server.domain.entities.QuestionEntity;
import org.gogame.server.repositories.QuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@EnableAsync
@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

    @Bean
    public CommandLineRunner initDatabase(QuestionRepository questionRepository) {
        return args -> {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = new ClassPathResource("questions.json").getInputStream();
            JsonNode root = mapper.readTree(is).path("questions");

            // wyczyść stare rekordy, żeby nie zostały puste
            questionRepository.deleteAll();

            List<QuestionEntity> questions = new ArrayList<>();
            if (root.isArray()) {
                for (JsonNode qNode : root) {
                    String text;
                    if (qNode.isTextual()) {
                        // gdybyś miał prostą listę stringów
                        text = qNode.asText();
                    } else {
                        // obecny format: obiekt z polem "prompt"
                        text = qNode.path("prompt").asText();
                    }
                    if (text != null && !text.isBlank()) {
                        questions.add(QuestionEntity.builder()
                                .question(text)
                                .build());
                    }
                }
            }

            questionRepository.saveAll(questions);
        };
    }

}
