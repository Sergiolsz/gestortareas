package com.app.managertask.infrastructure.controller;

import com.app.managertask.application.dto.request.CreateTaskRequest;
import com.app.managertask.application.dto.response.CreateTaskResponse;
import com.app.managertask.application.dto.response.GetTaskResponse;
import com.app.managertask.domain.port.input.TaskManagementUseCase;
import com.app.managertask.infrastructure.exception.TaskNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para manejar solicitudes HTTP relacionadas con tareas.
 * <p>
 * Expone los endpoints para crear, recuperar y listar tareas. Utiliza {@link TaskManagementUseCase}
 * para las operaciones de negocio relacionadas con las tareas.
 * </p>
 *
 * @see TaskManagementUseCase
 */
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task", description = "Endpoints to manage tasks")
@Slf4j
public class TaskController {

  private final TaskManagementUseCase taskManagementUseCase;

  /**
   * Construye un {@link TaskController} con el {@link TaskManagementUseCase} especificado.
   *
   * @param taskManagementUseCase el caso de uso para gestionar tareas
   */
  public TaskController(TaskManagementUseCase taskManagementUseCase) {
    this.taskManagementUseCase = taskManagementUseCase;
  }

  /**
   * Crea una nueva tarea.
   * <p>
   * Endpoint para crear una nueva tarea. Acepta un DTO con la información de la tarea, llama al
   * servicio para crear la tarea, y devuelve un DTO que contiene los detalles de la tarea creada.
   * </p>
   *
   * @param createTaskRequest DTO con la información de la tarea a crear
   * @return respuesta personalizada con los detalles de la tarea creada
   */
  @PostMapping
  @Operation(summary = "Create a new task", description = "Creates a new task in the database")
  @ApiResponse(responseCode = "201", description = "Task created successfully")
  @ApiResponse(responseCode = "400", description = "Invalid request")
  public ResponseEntity<CreateTaskResponse> createTask(
      @Parameter(description = "DTO containing the information for creating the task")
      @Valid @RequestBody CreateTaskRequest createTaskRequest) {
    log.info("Request to create task: {}", createTaskRequest);

    CreateTaskResponse response = taskManagementUseCase.createTask(createTaskRequest);

    log.info("Task created successfully with ID: {}", response.getId());
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * Recupera todas las tareas.
   * <p>
   * Endpoint para obtener el listado de todas las tareas existentes en la base de datos.
   * </p>
   *
   * @return lista de DTOs con la información de todas las tareas
   */
  @GetMapping
  @Operation(summary = "Retrieve all tasks", description = "Fetches a list of all tasks")
  @ApiResponse(responseCode = "200", description = "List of all tasks")
  public ResponseEntity<List<GetTaskResponse>> getAllTasks() {
    log.info("Fetching all tasks");

    List<GetTaskResponse> tasks = taskManagementUseCase.getAllTasks();

    log.info("Fetched {} tasks", tasks.size());
    return new ResponseEntity<>(tasks, HttpStatus.OK);
  }

  /**
   * Recupera una tarea por su ID.
   * <p>
   * Endpoint para la búsqueda de una tarea por su ID. Si la tarea no se encuentra, se devuelve un
   * error 404.
   * </p>
   *
   * @param id ID de la tarea a obtener
   * @return DTO con la información de la tarea
   * @throws TaskNotFoundException si no se encuentra ninguna tarea con el ID solicitado
   */
  @GetMapping("/{id}")
  @Operation(summary = "Retrieve a task by its ID",
      description = "Fetches a specific task by its ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Task found"),
      @ApiResponse(responseCode = "404", description = "Task not found")
  })
  public ResponseEntity<GetTaskResponse> getTaskById(
      @Parameter(description = "ID of the task to retrieve")
      @PathVariable String id) {
    log.info("Request to fetch task with ID: {}", id);

    GetTaskResponse taskResponse = taskManagementUseCase.getTaskById(id);

    log.info("Task found with ID: {}", id);
    return new ResponseEntity<>(taskResponse, HttpStatus.OK);
  }

}