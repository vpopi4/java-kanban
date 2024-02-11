package service.subtaskService;

import interfaces.repository.EpicRepository;
import interfaces.repository.SubtaskRepository;
import model.Task;
import service.HistoryService;
import service.IdGenerator;

public class InMemorySubtaskService extends AbstractSubtaskService {

    public InMemorySubtaskService(
            EpicRepository epicRepository,
            SubtaskRepository subtaskRepository,
            IdGenerator idGenerator,
            HistoryService<Task> historyService
    ) {
        super(epicRepository, subtaskRepository, idGenerator, historyService);
    }

}
