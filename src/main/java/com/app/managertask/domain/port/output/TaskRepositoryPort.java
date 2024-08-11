package com.app.managertask.domain.port.output;

import com.app.managertask.domain.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepositoryPort {

  Optional<Task> findById(String id);

  List<Task> findAll();

  Task save(Task task);
}