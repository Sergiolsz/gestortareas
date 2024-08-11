package com.app.managertask.infrastructure.repository;

import com.app.managertask.domain.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoTaskRepositoryInterface extends MongoRepository<Task, String> {

}