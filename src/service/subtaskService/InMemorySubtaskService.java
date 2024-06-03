package service.subtaskService;

import interfaces.HistoryManager;
import repository.InMemoryRepository;
import util.IdGenerator;

public class InMemorySubtaskService extends AbstractSubtaskService {
    public InMemorySubtaskService(
            InMemoryRepository repository,
            IdGenerator idGenerator,
            HistoryManager historyManager
    ) {
        super(repository, idGenerator, historyManager);
    }
}
