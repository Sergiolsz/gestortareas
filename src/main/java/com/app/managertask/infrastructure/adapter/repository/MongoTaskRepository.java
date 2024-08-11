package com.app.managertask.infrastructure.adapter.repository;

import com.app.managertask.domain.model.Task;
import com.app.managertask.domain.port.output.TaskRepositoryPort;
import com.app.managertask.infrastructure.repository.MongoTaskRepositoryInterface;
import java.util.Optional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoTaskRepository implements TaskRepositoryPort {

  private final MongoTaskRepositoryInterface repository;

  @Override
  public Optional<Task> findById(String id) {
    return repository.findById(id);
  }

  @Override
  public List<Task> findAll() {
    return repository.findAll();
  }

  @Override
  public Task save(Task task) {
    return repository.save(task);
  }
}