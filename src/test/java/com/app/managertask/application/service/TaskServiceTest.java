package com.app.managertask.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.app.managertask.application.dto.request.CreateTaskRequest;
import com.app.managertask.application.dto.response.CreateTaskResponse;
import com.app.managertask.application.dto.response.GetTaskResponse;
import com.app.managertask.application.mapper.TaskMapper;
import com.app.managertask.domain.model.Task;
import com.app.managertask.domain.port.output.TaskRepositoryPort;
import com.app.managertask.infrastructure.exception.TaskNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  private static final String ID_TASK = "1";
  private static final String TASKS_CACHE = "tasksCache";
  private static final String TASK_CACHE = "taskCache";

  @Mock
  private TaskRepositoryPort taskRepositoryPort;

  @Mock
  private TaskMapper taskMapper;

  @InjectMocks
  private TaskService taskService;

  private CacheManager cacheManager;

  private Task task;

  @BeforeEach
  void setUp() {
    cacheManager = new ConcurrentMapCacheManager(TASKS_CACHE, TASK_CACHE);

    task = Task.builder()
        .id(ID_TASK)
        .title("Test title task")
        .description("Test description task")
        .dueDate(LocalDateTime.now().plusDays(1))
        .build();
  }

  @Test
  void testCacheExists() {
    Cache cacheTasks = cacheManager.getCache(TASKS_CACHE);
    Cache cacheTask = cacheManager.getCache(TASK_CACHE);
    assertNotNull(cacheTasks, "The cacheTasks should be initialized and not null");
    assertNotNull(cacheTask, "The cacheTask should be initialized and not null");
  }

  @Test
  void testCreateTask() {
    CreateTaskRequest taskRequestDTO = new CreateTaskRequest();
    taskRequestDTO.setTitle("Test title task");
    taskRequestDTO.setDescription("Test description task");
    taskRequestDTO.setDueDate(LocalDateTime.now().plusDays(1));

    when(taskRepositoryPort.save(any(Task.class))).thenReturn(task);

    CreateTaskResponse response = taskService.createTask(taskRequestDTO);

    assertNotNull(response);
    assertEquals(ID_TASK, response.getId());
    assertEquals("Task created successfully", response.getMessage());

  }

  @Test
  void testGetAllTasks() {
    when(taskRepositoryPort.findAll()).thenReturn(Collections.singletonList(task));

    List<GetTaskResponse> tasks = taskService.getAllTasks();

    assertNotNull(tasks);
    assertEquals(1, tasks.size());
    assertEquals(ID_TASK, tasks.get(0).getId());

  }

  @Test
  void testGetTaskById() {
    when(taskRepositoryPort.findById(ID_TASK)).thenReturn(Optional.of(task));

    GetTaskResponse response = taskService.getTaskById(ID_TASK);

    assertNotNull(response);
    assertEquals(ID_TASK, response.getId());
  }

  @Test
  void testGetTaskById_TaskNotFound() {
    when(taskRepositoryPort.findById(ID_TASK)).thenReturn(Optional.empty());

    assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(ID_TASK));
  }
}