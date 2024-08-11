package com.app.managertask.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.managertask.application.dto.request.CreateTaskRequest;
import com.app.managertask.application.dto.response.CreateTaskResponse;
import com.app.managertask.application.dto.response.GetTaskResponse;
import com.app.managertask.domain.port.input.TaskManagementUseCase;
import com.app.managertask.infrastructure.exception.TaskNotFoundException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

  @InjectMocks
  private TaskController taskController;

  @Mock
  private TaskManagementUseCase taskManagementUseCase;

  private CreateTaskRequest createTaskRequest;
  private CreateTaskResponse createTaskResponse;
  private GetTaskResponse getTaskResponse;

  @BeforeEach
  void setUp() {
    createTaskRequest = new CreateTaskRequest();
    createTaskRequest.setTitle("Test Task");
    createTaskRequest.setDescription("Test Description");

    createTaskResponse = CreateTaskResponse.builder()
        .id("12345")
        .build();

    getTaskResponse = GetTaskResponse.builder()
        .id("12345")
        .title("Test Task")
        .description("Test Description")
        .build();
  }

  @Test
  void testCreateTask() {
    when(taskManagementUseCase.createTask(createTaskRequest)).thenReturn(createTaskResponse);

    ResponseEntity<CreateTaskResponse> response = taskController.createTask(
        createTaskRequest);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(createTaskResponse, response.getBody());
    verify(taskManagementUseCase).createTask(createTaskRequest);
  }

  @Test
  void testGetAllTasks() {
    when(taskManagementUseCase.getAllTasks()).thenReturn(
        Collections.singletonList(getTaskResponse));

    ResponseEntity<List<GetTaskResponse>> response = taskController.getAllTasks();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
    assertEquals(getTaskResponse, response.getBody().get(0));
    verify(taskManagementUseCase).getAllTasks();
  }

  @Test
  void testGetTaskById() {
    when(taskManagementUseCase.getTaskById("12345")).thenReturn(getTaskResponse);

    ResponseEntity<GetTaskResponse> response = taskController.getTaskById("12345");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(getTaskResponse, response.getBody());
    verify(taskManagementUseCase).getTaskById("12345");
  }

  @Test
  void testGetTaskById_TaskNotFound() {

    when(taskManagementUseCase.getTaskById("122345")).thenThrow(
        new TaskNotFoundException("Task not found"));

    TaskNotFoundException thrownException = assertThrows(TaskNotFoundException.class, () ->
        taskController.getTaskById("122345")
    );

    assertEquals("Task not found", thrownException.getMessage());
  }
}