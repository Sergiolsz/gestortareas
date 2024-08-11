package com.app.managertask.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetTaskResponse {

  @Schema(description = "Unique identifier of the task", example = "12345")
  String id;

  @Schema(description = "Title of the task", example = "Team Meeting")
  String title;

  @Schema(description = "Detailed description of the task", example = "Prepare agenda and send meeting invites for the team meeting.")
  String description;

  @Schema(description = "Due date of the task in format dd/MM/yyyy HH:mm:ss", example = "15/08/2024 12:30:00")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
  LocalDateTime dueDate;

  @Schema(description = "Tags associated with the task", example = "[\"meeting\", \"team\"]")
  String[] tags;

  @Schema(description = "Current status of the task", example = "Active")
  String status;
}