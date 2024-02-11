package service.subtaskService;

import interfaces.repository.EpicRepository;
import interfaces.repository.SubtaskRepository;
import service.IdGenerator;

public class InMemorySubtaskService extends AbstractSubtaskService {

    public InMemorySubtaskService(
            EpicRepository epicRepository,
            SubtaskRepository subtaskRepository,
            IdGenerator idGenerator) {
        super(epicRepository, subtaskRepository, idGenerator);
    }

}
