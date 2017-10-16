package xyz.deseteral.djlama.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionMapper {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Error> handleException(ResourceNotFoundException exception) {
        Error error = new Error.Builder()
            .withStatus(404)
            .withKey("RESOURCE_NOT_FOUND")
            .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}

