package com.ticketis.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationErrorResponse {
    private String code;
    private String message;
    private Instant timestamp;
    private Integer status;
    private Integer errorCount;
    private List<String> errors; 
    private Map<String, String> fieldErrors;
    
    public static ValidationErrorResponse of(String code, String message, HttpStatus status) {
        return ValidationErrorResponse.builder()
            .code(code)
            .message(message)
            .timestamp(Instant.now())
            .status(status.value())
            .build();
    }
}