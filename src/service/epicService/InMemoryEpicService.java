package service.epicService;

import interfaces.repository.EpicRepository;
import interfaces.repository.SubtaskRepository;
import model.Task;
import service.HistoryService;
import service.IdGenerator;

public class InMemoryEpicService extends AbstractEpicService {
    public InMemoryEpicService(
            EpicRepository epicRepository,
            SubtaskRepository subtaskRepository,
            IdGenerator idGenerator,
            HistoryService<Task> historyService
    ) {
        super(
                epicRepository,
                subtaskRepository,
                idGenerator,
                historyService
        );
    }
}
