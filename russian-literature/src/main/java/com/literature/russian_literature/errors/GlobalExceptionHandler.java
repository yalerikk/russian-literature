package com.literature.russian_literature.errors;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Обработка ошибок валидации (@Valid)
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

        log.warn("Validation error: {}", errorMessage);

        var errorDto = new ErrorResponse(
                "Ошибка валидации данных",
                errorMessage,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    // Обработка некорректного JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON request: {}", ex.getMessage());

        String userMessage = "Неверный формат JSON данных";
        String detailedMessage = "Проверьте корректность всех полей и их типов";

        // Более конкретные сообщения для частых ошибок
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("Enum")) {
                userMessage = "Некорректное значение для одного из полей";
                detailedMessage = "Проверьте значения полей с предопределенными вариантами (жанр, тип хранения и т.д.)";
            } else if (ex.getMessage().contains("LocalDate")) {
                userMessage = "Неверный формат даты";
                detailedMessage = "Используйте формат ГГГГ-ММ-ДД (например: 2021-11-11)";
            }
        }

        var errorDto = new ErrorResponse(userMessage, detailedMessage, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    // Обработка неверных типов параметров
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn("Type mismatch for parameter: {}", ex.getName());

        var errorDto = new ErrorResponse(
                "Неверный тип параметра",
                String.format("Параметр '%s' должен быть типа %s", ex.getName(), ex.getRequiredType().getSimpleName()),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    // Обработка отсутствующих параметров
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
        log.warn("Missing parameter: {}", ex.getParameterName());

        var errorDto = new ErrorResponse(
                "Отсутствует обязательный параметр",
                String.format("Параметр '%s' обязателен", ex.getParameterName()),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    // Обработка 404 - не найден endpoint
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.warn("No handler found for: {} {}", ex.getHttpMethod(), ex.getRequestURL());

        var errorDto = new ErrorResponse(
                "Ресурс не найден",
                String.format("Метод %s не поддерживается для URL: %s", ex.getHttpMethod(), ex.getRequestURL()),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    // Обработка EntityNotFoundException (более специфичное сообщение)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("Entity not found: {}", e.getMessage());

        var errorDto = new ErrorResponse(
                "Объект не найден",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    // Обработка ошибок аутентификации
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        log.warn("Authentication failed: {}", e.getMessage());

        var errorDto = new ErrorResponse(
                "Ошибка аутентификации",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    // Обработка бизнес-логики (IllegalArgumentException, IllegalStateException)
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleBusinessLogicException(RuntimeException e) {
        log.warn("Business logic error: {}", e.getMessage());

        var errorDto = new ErrorResponse(
                "Ошибка в данных",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    // Общая обработка всех остальных исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        log.error("Internal server error", e);

        var errorDto = new ErrorResponse(
                "Внутренняя ошибка сервера",
                "Произошла непредвиденная ошибка. Пожалуйста, попробуйте позже.",
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }

    // Кастомное исключение для ошибок аутентификации
    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }
    }
}