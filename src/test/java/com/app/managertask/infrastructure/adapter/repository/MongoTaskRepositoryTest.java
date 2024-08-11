package com.app.managertask.infrastructure.adapter.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.managertask.domain.model.Task;
import com.app.managertask.infrastructure.repository.MongoTaskRepositoryInterface;
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

@ExtendWith(MockitoExtension.class)
class MongoTaskRepositoryTest {

  @Mock
  private MongoTaskRepositoryInterface mongoTaskRepositoryInterface;

  @InjectMocks
  private MongoTaskRepository mongoTaskRepository;

  private Task task;
  private final String taskId = "1";

  @BeforeEach
  void setUp() {
    task = Task.builder()
        .id(taskId)
        .title("Test Task")
        .description("Test Description")
        .dueDate(LocalDateTime.now().plusDays(1))
        .build();
  }

  @Test
  void testFindById() {
    when(mongoTaskRepositoryInterface.findById(taskId)).thenReturn(Optional.of(task));

    Optional<Task> foundTask = mongoTaskRepository.findById(taskId);

    assertTrue(foundTask.isPresent(), "Task should be found");
    assertEquals(taskId, foundTask.get().getId(), "Task ID should match");
  }

  @Test
  void testFindById_TaskNotFound() {
    when(mongoTaskRepositoryInterface.findById(taskId)).thenReturn(Optional.empty());

    Optional<Task> foundTask = mongoTaskRepository.findById(taskId);

    assertFalse(foundTask.isPresent(), "Task should not be found");
  }

  @Test
  void testFindAll() {
    List<Task> tasks = Collections.singletonList(task);
    when(mongoTaskRepositoryInterface.findAll()).thenReturn(tasks);

    List<Task> foundTasks = mongoTaskRepository.findAll();

    assertNotNull(foundTasks, "Task list should not be null");
    assertEquals(1, foundTasks.size(), "Task list should contain one task");
    assertEquals(taskId, foundTasks.get(0).getId(), "Task ID should match");
  }

  @Test
  void testSave() {
    when(mongoTaskRepositoryInterface.save(task)).thenReturn(task);

    Task savedTask = mongoTaskRepository.save(task);

    assertNotNull(savedTask, "Saved task should not be null");
    assertEquals(taskId, savedTask.getId(), "Task ID should match");
    verify(mongoTaskRepositoryInterface).save(task);
  }
}