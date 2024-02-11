package service.taskService;

import interfaces.repository.TaskRepository;
import service.IdGenerator;

public class InMemoryTaskService extends AbstractTaskService {
    public InMemoryTaskService(TaskRepository repository, IdGenerator idGenerator) {
        super(repository, idGenerator);
    }
}
