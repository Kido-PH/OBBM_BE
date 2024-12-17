package com.springboot.obbm.configuration;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class CustomJwtException extends AuthenticationException {

    private final HttpStatus status;

    public CustomJwtException(String msg) {
        super(msg);
        this.status = HttpStatus.UNAUTHORIZED; // Luôn trả về 401
    }

}