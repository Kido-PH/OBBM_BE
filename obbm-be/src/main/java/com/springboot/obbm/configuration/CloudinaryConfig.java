package com.springboot.obbm.configuration;

import com.cloudinary.Cloudinary;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @NonFinal
    @Value("${cloudinary.cloud-name}")
    protected String cloudName;

    @NonFinal
    @Value("${cloudinary.api-key}")
    protected String apiKey;

    @NonFinal
    @Value("${cloudinary.api-secret}")
    protected String apiSecret;

    @Bean
    public Cloudinary configKey() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        return new Cloudinary(config);
    }
}
