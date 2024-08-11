package com.app.managertask.domain.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Builder
@Document(collection = "tasks")
public class Task {

  @Id
  String id;
  String title;
  String description;
  LocalDateTime dueDate;
  String[] tags;

  /**
   * Comprueba el estado de una tarea en virtud de la fecha de vencimiento. El estado es "Expired"
   * si la fecha de vencimiento es anterior a la fecha actual, y "Active" en caso contrario.
   *
   * @return "Expired" o "Active"
   */
  public String getStatus() {
    return dueDate.isBefore(LocalDateTime.now()) ? "Expired" : "Active";
  }
}