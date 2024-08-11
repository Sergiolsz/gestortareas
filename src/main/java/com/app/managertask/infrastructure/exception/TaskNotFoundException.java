package com.app.managertask.infrastructure.exception;

/**
 * Excepción para gestionar el proceso de cuando no se encuentra una tarea con el ID solicitado.
 */
public class TaskNotFoundException extends RuntimeException {

  public TaskNotFoundException(String message) {
    super(message);
  }
}