package com.app.managertask.application.service;

import com.app.managertask.application.dto.request.CreateTaskRequest;
import com.app.managertask.application.dto.response.CreateTaskResponse;
import com.app.managertask.application.dto.response.GetTaskResponse;
import com.app.managertask.application.mapper.TaskMapper;
import com.app.managertask.domain.model.Task;
import com.app.managertask.domain.port.input.TaskManagementUseCase;
import com.app.managertask.domain.port.output.TaskRepositoryPort;
import com.app.managertask.infrastructure.exception.TaskNotFoundException;
import com.app.managertask.infrastructure.repository.MongoTaskRepositoryInterface;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Servicio para la gestión de tareas en la aplicación.
 * <p>
 * Maneja las operaciones CRUD para las tareas: Creación de nuevas tareas, Obtención de todas las
 * tareas Obtención de una tarea por su ID. Utiliza {@link MongoTaskRepositoryInterface} para las
 * operaciones con la base de datos y {@link TaskMapper} para la conversión entre entidades y DTOs.
 * </p>
 *
 * @see MongoTaskRepositoryInterface
 * @see TaskMapper
 */
@Service
@Slf4j
public class TaskService implements TaskManagementUseCase {

  private final TaskRepositoryPort taskRepositoryPort;
  private final TaskMapper taskMapper = TaskMapper.INSTANCE;

  public TaskService(TaskRepositoryPort taskRepositoryPort) {
    this.taskRepositoryPort = taskRepositoryPort;
  }

  /**
   * Crea una nueva tarea en la base de datos.
   * <p>
   * Convierte el {@link CreateTaskRequest} en una entidad {@link Task}, la guarda en la base de
   * datos y devuelve un {@link CreateTaskResponse} con la información de la tarea creada.
   * </p>
   *
   * @param createTaskRequest DTO con la información de la tarea
   * @return DTO con el ID de la tarea creada y mensaje de respuesta
   */
  @Override
  @CacheEvict(value = "tasksCache", allEntries = true)
  public CreateTaskResponse createTask(CreateTaskRequest createTaskRequest) {
    log.info("Creating task with title: {}", createTaskRequest.getTitle());

    Task task = taskMapper.mapToTaskEntity(createTaskRequest);

    Task savedTask = taskRepositoryPort.save(task);

    log.info("Task created successfully with ID: {}", savedTask.getId());

    return CreateTaskResponse.builder()
        .id(savedTask.getId())
        .message("Task created successfully")
        .build();
  }

  /**
   * Lista todas las tareas que existen en la base de datos.
   * <p>
   * Obtiene todas las tareas y las mapea en una lista de DTOs {@link GetTaskResponse}.
   * </p>
   *
   * @return lista de DTOs con la información de todas las tareas
   */
  @Override
  @Cacheable(value = "tasksCache")
  public List<GetTaskResponse> getAllTasks() {
    log.info("Fetching all tasks");

    return taskRepositoryPort.findAll().stream()
        .map(taskMapper::mapToTaskResponseWithStatus)
        .peek(task -> log.info("Mapped task with ID: {} and status: {}",
            task.getId(),
            task.getStatus()))
        .toList();
  }

  /**
   * Obtener una tarea por su ID.
   * <p>
   * Busca una tarea por su ID. Si la tarea no se encuentra, se lanza una
   * {@link TaskNotFoundException}. Si existe, la tarea es mapeada a un DTO
   * {@link GetTaskResponse}.
   * </p>
   *
   * @param id ID de la tarea a obtener
   * @return el DTO con la información de la tarea
   * @throws TaskNotFoundException si no se encuentra una tarea con el ID
   */
  @Override
  @Cacheable(value = "taskCache", key = "#id")
  public GetTaskResponse getTaskById(String id) {
    log.info("Fetching task with ID: {}", id);

    Task task = taskRepositoryPort.findById(id)
        .orElseThrow(() -> {
          log.error("Task not found with ID: {}", id);
          return new TaskNotFoundException("Task not found with ID: " + id);
        });

    log.info("Task found with ID: {}", id);

    return taskMapper.mapToTaskResponseWithStatus(task);
  }
}