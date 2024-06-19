package service.subtaskService;

import interfaces.HistoryManager;
import model.Subtask;
import repository.InMemoryRepository;
import util.IdGenerator;
import util.TaskableValidator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class InMemorySubtaskService extends AbstractSubtaskService {
    protected InMemoryRepository repository;

    public InMemorySubtaskService(
            InMemoryRepository repository,
            IdGenerator idGenerator,
            HistoryManager historyManager
    ) {
        super(repository, idGenerator, historyManager);
        this.repository = repository;
    }

    @Override
    public Subtask create(Integer epicId, String name, String description, Duration duration, LocalDateTime startTime) {
        Subtask subtask = super.create(epicId, name, description, duration, startTime);

        try {
            TaskableValidator.checkIntersectionClosestSearch(
                    subtask,
                    repository.getPrioritizedTasksInTree()
            );
        } catch (TaskableValidator.IntersectionException e) {
            remove(subtask.getId());
            throw e;
        }

        return subtask;
    }

    @Override
    public Subtask update(Subtask subtask) throws NoSuchElementException {
        subtask = super.update(subtask);


        try {
            TaskableValidator.checkIntersectionClosestSearch(
                    subtask,
                    repository.getPrioritizedTasksInTree()
            );
        } catch (TaskableValidator.IntersectionException e) {
            remove(subtask.getId());
            throw e;
        }


        return subtask;
    }
}
