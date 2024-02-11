package service.taskService;

import interfaces.repository.TaskRepository;
import model.Task;
import util.History;
import util.IdGenerator;

public class InMemoryTaskService extends AbstractTaskService {
    public InMemoryTaskService(
            TaskRepository repository,
            IdGenerator idGenerator,
            History<Task> historyService
    ) {
        super(repository, idGenerator, historyService);
    }
}
