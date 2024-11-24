package com.springboot.obbm.configuration;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PayOSConfiguration {
    @NonFinal
    @Value("${payos.client-id}")
    protected String clientId;

    @NonFinal
    @Value("${payos.api-key}")
    protected String apiKey;

    @NonFinal
    @Value("${payos.checksum-key}")
    protected String checksumKey;

    @Bean
    public PayOS payOS() {
        return new PayOS(clientId, apiKey, checksumKey);
    }

}
