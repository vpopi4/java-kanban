package service.epicService;

import interfaces.HistoryManager;
import repository.InMemoryRepository;
import util.IdGenerator;

public class InMemoryEpicService extends AbstractEpicService {
    public InMemoryEpicService(
            InMemoryRepository repository,
            IdGenerator idGenerator,
            HistoryManager historyManager
    ) {
        super(repository, idGenerator, historyManager);
    }
}
