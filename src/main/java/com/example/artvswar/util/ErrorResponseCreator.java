package com.example.artvswar.util;

import com.example.artvswar.dto.response.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class ErrorResponseCreator {
    public ResponseEntity<ErrorResponse> createResponse(Exception ex, HttpStatus httpStatus) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now().toString());
        errorResponse.setError(httpStatus.getReasonPhrase());
        errorResponse.setException(ex.getClass().getSimpleName());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(httpStatus.value());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
