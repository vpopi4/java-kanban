package service.taskService;

import interfaces.repository.TaskRepository;
import model.Task;
import service.HistoryService;
import util.IdGenerator;

public class InMemoryTaskService extends AbstractTaskService {
    public InMemoryTaskService(
            TaskRepository repository,
            IdGenerator idGenerator,
            HistoryService<Task> historyService
    ) {
        super(repository, idGenerator, historyService);
    }
}
