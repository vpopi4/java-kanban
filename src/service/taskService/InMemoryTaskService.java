package service.taskService;

import interfaces.HistoryManager;
import interfaces.repository.TaskRepository;
import model.Task;
import util.IdGenerator;

public class InMemoryTaskService extends AbstractTaskService {
    public InMemoryTaskService(
            TaskRepository repository,
            IdGenerator idGenerator,
            HistoryManager<Task> historyService
    ) {
        super(repository, idGenerator, historyService);
    }
}
