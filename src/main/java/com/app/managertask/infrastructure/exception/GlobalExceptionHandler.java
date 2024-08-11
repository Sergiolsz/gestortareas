package com.app.managertask.infrastructure.exception;

import java.time.DateTimeException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones para la aplicación.
 * <p>
 * Proporciona un manejo centralizado de diferentes tipos de excepciones a lo largo de la aplicación.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Maneja las excepciones de validación para parámetros de solicitud inválidos.
   *
   * @param ex Instancia de MethodArgumentNotValidException.
   * @return ResponseEntity que contiene los errores de validación con el estado BAD_REQUEST.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getAllErrors()
        .forEach((error) -> {
          String fieldName = ((FieldError) error).getField();
          String errorMessage = error.getDefaultMessage();
          errors.put(fieldName, errorMessage);
        });

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  /**
   * Maneja las excepciones para mensajes de solicitud inválidos.
   *
   * @param ex Instancia de HttpMessageNotReadableException.
   * @return ResponseEntity que contiene mensajes de error con el estado BAD_REQUEST.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {

    Throwable cause = ex.getMostSpecificCause();
    Map<String, String> errors = new HashMap<>();

    if (cause instanceof DateTimeException) {
      errors.put("dueDate", "The date must be in the format dd/MM/yyyy HH:mm:ss and be valid.");
    } else {
      errors.put("error", "Error in the request. Please check the input data.");
    }

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  /**
   * Maneja las excepciones de tarea no encontrada.
   *
   * @param ex Instancia de TaskNotFoundException.
   * @return ResponseEntity que contiene el mensaje de error con el estado NOT_FOUND.
   */
  @ExceptionHandler(TaskNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  /**
   * Maneja todas las demás excepciones no manejadas por los métodos anteriores.
   *
   * @param ex Instancia de Exception.
   * @return ResponseEntity que contiene el mensaje de error con el estado INTERNAL_SERVER_ERROR.
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<String> handleGeneralException(Exception ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}