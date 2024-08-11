package com.app.managertask.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateTaskResponse {

  @Schema(description = "Message indicating the result of the task creation", example = "Task created successfully")
  String message;

  @Schema(description = "Unique identifier of the task", example = "12345")
  String id;

}