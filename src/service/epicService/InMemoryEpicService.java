package service.epicService;

import interfaces.HistoryManager;
import interfaces.repository.EpicRepository;
import interfaces.repository.SubtaskRepository;
import model.Task;
import util.IdGenerator;

public class InMemoryEpicService extends AbstractEpicService {
    public InMemoryEpicService(
            EpicRepository epicRepository,
            SubtaskRepository subtaskRepository,
            IdGenerator idGenerator,
            HistoryManager<Task> historyService
    ) {
        super(
                epicRepository,
                subtaskRepository,
                idGenerator,
                historyService
        );
    }
}
