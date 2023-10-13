package com.example.artvswar.controller;

import com.example.artvswar.dto.response.error.AllErrorFieldResponse;
import com.example.artvswar.dto.response.error.ErrorFieldResponse;
import com.example.artvswar.dto.response.error.ErrorResponse;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.exception.CloudinaryCredentialException;
import com.example.artvswar.exception.PaintingNotAvailableException;
import com.example.artvswar.exception.ShippingNotProcessingException;
import com.example.artvswar.util.ErrorResponseCreator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private final ErrorResponseCreator errorResponseCreator;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        List<ErrorFieldResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(er -> new ErrorFieldResponse(er.getDefaultMessage(), er.getField(),
                        er.getRejectedValue(), er.isBindingFailure(), er.getCode()))
                .collect(Collectors.toList());
        AllErrorFieldResponse response = new AllErrorFieldResponse();
        response.setTimestamp(new Date());
        response.setStatus(status.value());
        response.setError(status.getReasonPhrase());
        response.setFieldErrorCount(ex.getFieldErrorCount());
        response.setErrors(errors);
        return new ResponseEntity<>(response, headers, status);
    }

    @ExceptionHandler(value = {AppEntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> entityNotFoundExceptionHandler(AppEntityNotFoundException ex) {
        return errorResponseCreator.createResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {PaintingNotAvailableException.class})
    public ResponseEntity<ErrorResponse> paintingNotAvailableExceptionHandler(
            PaintingNotAvailableException ex) {
        return errorResponseCreator.createResponse(ex, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {ShippingNotProcessingException.class})
    public ResponseEntity<ErrorResponse> shippingNotProcessingExceptionHandler(
            ShippingNotProcessingException ex) {
        return errorResponseCreator.createResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {CloudinaryCredentialException.class})
    public ResponseEntity<ErrorResponse> cloudinaryCredentialsExceptionHandler(
            CloudinaryCredentialException ex) {
        return errorResponseCreator.createResponse(ex, HttpStatus.NOT_ACCEPTABLE);
    }


}
