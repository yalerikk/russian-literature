package com.literature.russian_literature.errors;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    String field = error.getField();
                    String message = error.getDefaultMessage();
                    return String.format("%s: %s", field, message);
                })
                .collect(Collectors.joining("; "));

        LOG.warn("Validation error: {}", errorMessage);

        var errorDto = new ErrorResponse(
                "Data validation error",
                errorMessage,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        LOG.warn("Malformed JSON request: {}", ex.getMessage());

        String userMessage = "Invalid JSON data format";
        String detailedMessage = "Check all fields and their types for correctness";

        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("Enum")) {
                userMessage = "Invalid value for one of the fields";
                detailedMessage = "Check values for fields with predefined options (genre, format, etc.)";
            } else if (ex.getMessage().contains("LocalDate")) {
                userMessage = "Invalid date format";
                detailedMessage = "Use format YYYY-MM-DD (e.g., 2021-11-11)";
            }
        }

        var errorDto = new ErrorResponse(userMessage, detailedMessage, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        LOG.warn("Type mismatch for parameter: {}", ex.getName());

        var errorDto = new ErrorResponse(
                "Invalid parameter type",
                String.format("Parameter '%s' must be of type %s", ex.getName(), ex.getRequiredType().getSimpleName()),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
        LOG.warn("Missing parameter: {}", ex.getParameterName());

        var errorDto = new ErrorResponse(
                "Required parameter missing",
                String.format("Parameter '%s' is required", ex.getParameterName()),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        LOG.warn("No handler found for: {} {}", ex.getHttpMethod(), ex.getRequestURL());

        var errorDto = new ErrorResponse(
                "Resource not found",
                String.format("Method %s is not supported for URL: %s", ex.getHttpMethod(), ex.getRequestURL()),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        LOG.warn("Entity not found: {}", e.getMessage());

        var errorDto = new ErrorResponse(
                "Object not found",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        LOG.warn("Authentication failed: {}", e.getMessage());

        var errorDto = new ErrorResponse(
                "Authentication error",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleBusinessLogicException(RuntimeException e) {
        LOG.warn("Business logic error: {}", e.getMessage());

        var errorDto = new ErrorResponse(
                "Data error",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        LOG.error("Internal server error", e);

        var errorDto = new ErrorResponse(
                "Internal server error",
                "An unexpected error occurred. Please try again later.",
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Unauthorized", e.getMessage()));
    }

    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }
    }
}
