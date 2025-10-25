package org.gogame.server.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * Q: Po kuj my to potrzebujemy?
 * A: To tool do "wybierania i łączenia pól" między różnymi entity w Java.
 *    Często zdarza się, że w zapytaniu musimy zwrócić tylko niektóre pola z entity,
 *    czyli dany mapper pomaga kiedy zwracanie całego entity byłoby nadmiarowe.
 *
 *    Niech na razie zostanie
 */

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;
    }
}
