package com.app.managertask.application.mapper;

import com.app.managertask.application.dto.request.CreateTaskRequest;
import com.app.managertask.application.dto.response.GetTaskResponse;
import com.app.managertask.domain.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {

  TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

  /**
   * Mapeo de un DTO de solicitud de creación de una tarea a una entidad Task.
   *
   * @param createTaskRequest DTO de la Tarea
   * @return Task
   */
  Task mapToTaskEntity(CreateTaskRequest createTaskRequest);

  /**
   * Mapeo de una Task en un DTO de respuesta.
   *
   * @param task Task
   * @return GetTaskResponse
   */
  @Mapping(target = "status", expression = "java(task.getStatus())")
  GetTaskResponse mapToTaskResponseWithStatus(Task task);

}