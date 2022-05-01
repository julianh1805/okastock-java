package com.julianhusson.okastock.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.mail.MessagingException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e){
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e){
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e){
        ApiException apiException = new ApiException(
                e.getConstraintViolations().stream().findFirst().get().getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException e){
        ApiException apiException = new ApiException(
                "Le champ " + e.getPath().get(0).getFieldName() + " contient une donnée invalide.",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(InvalidRegexException.class)
    public ResponseEntity<Object> handleInvalidRegexException(InvalidRegexException e){
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException e){
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(BadUserException.class)
    public ResponseEntity<Object> handleBadUserException(BadUserException e){
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.FORBIDDEN,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Object> handleTokenExpiredException(TokenExpiredException e){
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<Object> handleMessageException(MessagingException e){
        ApiException apiException = new ApiException(
                "Une erreur est survenue pendant le creation du compte. Veuillez reessayer ultérieurement.",
                HttpStatus.BAD_GATEWAY,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    @ExceptionHandler(StorageFileException.class)
    public ResponseEntity<Object> handleStorageFileException(StorageFileException e){
        ApiException apiException = new ApiException(
                "Une erreur est survenue pendant le traitement de(s) image(s).",
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, apiException.httpStatus());
    }

    public record ApiException(String message,  HttpStatus httpStatus, LocalDateTime localDateTime) {}
}
