package br.com.gusapi.exceptions.handler;

import br.com.gusapi.exceptions.ExceptionResponse;
import br.com.gusapi.exceptions.InvalidJwtAuthenticationException;
import br.com.gusapi.exceptions.ResourceNotFoundException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {

        ExceptionResponse cexceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(cexceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleTokenExpiredExceptions(ResourceNotFoundException ex, WebRequest request) {

        ExceptionResponse cexceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(cexceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidJwtAuthenticationException.class)
    public final ResponseEntity<ExceptionResponse> handleInvalidJwtAuthenticationExceptions(InvalidJwtAuthenticationException ex, WebRequest request) {

        ExceptionResponse cexceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(cexceptionResponse, HttpStatus.FORBIDDEN);
    }

}
