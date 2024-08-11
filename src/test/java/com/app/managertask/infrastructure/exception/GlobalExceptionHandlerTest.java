package com.app.managertask.infrastructure.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.DateTimeException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @InjectMocks
  private GlobalExceptionHandler globalExceptionHandler;

  @Mock
  private MethodArgumentNotValidException methodArgumentNotValidException;

  @Mock
  private FieldError fieldError;

  @Mock
  private BindingResult bindingResult;

  @Mock
  private HttpMessageNotReadableException httpMessageNotReadableException;

  @Test
  void testHandleValidationExceptions() {

    when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
    when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));
    when(fieldError.getField()).thenReturn("fieldName");
    when(fieldError.getDefaultMessage()).thenReturn("Default message");

    ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(
        methodArgumentNotValidException);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    assertEquals("Default message", response.getBody().get("fieldName"));
  }

  @Test
  void testHandleHttpMessageNotReadableException_DateTimeException() {

    Throwable cause = new DateTimeException("Invalid date format");
    when(httpMessageNotReadableException.getMostSpecificCause()).thenReturn(cause);

    ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleHttpMessageNotReadableException(
        httpMessageNotReadableException);

    Map<String, String> expectedErrors = new HashMap<>();
    expectedErrors.put("dueDate", "The date must be in the format dd/MM/yyyy HH:mm:ss and be valid.");

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(expectedErrors, response.getBody());
  }

  @Test
  void testHandleHttpMessageNotReadableException_OtherException() {

    Throwable cause = new IllegalArgumentException("Invalid argument");
    when(httpMessageNotReadableException.getMostSpecificCause()).thenReturn(cause);

    ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleHttpMessageNotReadableException(
        httpMessageNotReadableException);

    Map<String, String> expectedErrors = new HashMap<>();
    expectedErrors.put("error", "Error in the request. Please check the input data.");

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(expectedErrors, response.getBody());
  }
  @Test
  void testHandleTaskNotFoundException() {

    TaskNotFoundException ex = new TaskNotFoundException("Task not found");

    ResponseEntity<String> response = globalExceptionHandler.handleTaskNotFoundException(ex);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Task not found", response.getBody());
  }

  @Test
  void testHandleGeneralException() {

    Exception ex = new Exception("General error");

    ResponseEntity<String> response = globalExceptionHandler.handleGeneralException(ex);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("General error", response.getBody());
  }
}