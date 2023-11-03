package com.school.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomException extends RuntimeException {
    private int statusCode;

    public CustomException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}