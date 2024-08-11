package com.app.managertask.domain.port.input;

import com.app.managertask.application.dto.request.CreateTaskRequest;
import com.app.managertask.application.dto.response.CreateTaskResponse;
import com.app.managertask.application.dto.response.GetTaskResponse;
import java.util.List;

public interface TaskManagementUseCase {

  CreateTaskResponse createTask(CreateTaskRequest createTaskRequest);

  List<GetTaskResponse> getAllTasks();

  GetTaskResponse getTaskById(String id);
}
