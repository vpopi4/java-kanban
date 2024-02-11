package service.epicService;

import interfaces.repository.EpicRepository;
import interfaces.repository.SubtaskRepository;
import model.Task;
import util.History;
import util.IdGenerator;

public class InMemoryEpicService extends AbstractEpicService {
    public InMemoryEpicService(
            EpicRepository epicRepository,
            SubtaskRepository subtaskRepository,
            IdGenerator idGenerator,
            History<Task> historyService
    ) {
        super(
                epicRepository,
                subtaskRepository,
                idGenerator,
                historyService
        );
    }
}
