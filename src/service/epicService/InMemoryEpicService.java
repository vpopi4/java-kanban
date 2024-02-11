package service.epicService;

import interfaces.repository.EpicRepository;
import interfaces.repository.SubtaskRepository;
import service.IdGenerator;

public class InMemoryEpicService extends AbstractEpicService {
    public InMemoryEpicService(
            EpicRepository epicRepository,
            SubtaskRepository subtaskRepository,
            IdGenerator idGenerator) {
        super(epicRepository, subtaskRepository, idGenerator);
    }
}
