package com.springboot.obbm.configuration;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiRunnerConfig {
    @Autowired
    private ChatLanguageModel model;

    @Bean(name = "mainApplicationRunner")
    public ApplicationRunner applicationRunner() {
        return args -> {
            try {
                String responseText = model.generate("Hello, how are you?");
                System.out.println(responseText);
            } catch (Exception e) {
                System.err.println("Error while interacting with AI: " + e.getMessage());
            }
        };
    }
}
