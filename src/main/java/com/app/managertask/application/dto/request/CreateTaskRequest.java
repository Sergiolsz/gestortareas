package com.app.managertask.application.dto.request;

import com.app.managertask.application.validation.ValidDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CreateTaskRequest {

  @Schema(description = "Title of the task", example = "Team Meeting")
  private String title;

  @Schema(description = "Description of the task", example = "Prepare agenda and send meeting invites for the team meeting.")
  private String description;

  @ValidDate(message = "The date must be valid and in the future.")
  @Schema(description = "Due date of the task in format dd/MM/yyyy HH:mm:ss", example = "15/08/2024 12:30:00")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime dueDate;

  @Schema(description = "Tags associated with the task", example = "[\"meeting\", \"team\"]")
  private String[] tags;
}