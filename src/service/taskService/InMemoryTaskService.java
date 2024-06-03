package service.taskService;

import interfaces.HistoryManager;
import repository.InMemoryRepository;
import util.IdGenerator;

public class InMemoryTaskService extends AbstractTaskService {
    public InMemoryTaskService(
            InMemoryRepository repository,
            IdGenerator idGenerator,
            HistoryManager historyManager
    ) {
        super(repository, idGenerator, historyManager);
    }
}
