package com.julianhusson.okastock.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e){
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e){
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e){
        ApiException apiException = new ApiException(
                e.getConstraintViolations().stream().findFirst().get().getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(value = InvalidFormatException.class)
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException e){
        ApiException apiException = new ApiException(
                "Le champ " + e.getPath().get(0).getFieldName() + " contient une donnée invalide.",
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(value = InvalidRegexException.class)
    public ResponseEntity<Object> handleInvalidRegexException(InvalidRegexException e){
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException e){
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }


    public record ApiException(String message,  HttpStatus httpStatus, ZonedDateTime zonedDateTime) {}
}
