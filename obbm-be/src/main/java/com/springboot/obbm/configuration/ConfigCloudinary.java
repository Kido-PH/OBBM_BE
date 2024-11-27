package com.springboot.obbm.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigCloudinary {

    @Bean
    public Cloudinary configKey() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dpu6e4ebx");
        config.put("api_key", "461762216833814");
        config.put("api_secret", "pxMYhsccAckpcGS8nr0HaWP2Ry0");
        return new Cloudinary(config);
    }
}
