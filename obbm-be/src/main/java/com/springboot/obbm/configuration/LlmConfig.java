package com.springboot.obbm.configuration;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LlmConfig {
    @Value("${open-ai.chat-model.api-key}")
    protected String apiKey;

    @Value("${open-ai.chat-model.model-name}")
    protected String modelName;

    @Value("${open-ai.chat-model.temperature}")
    protected double temperature;

    @Bean
    public OpenAiChatModel openAiChatModel() {
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(temperature)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
