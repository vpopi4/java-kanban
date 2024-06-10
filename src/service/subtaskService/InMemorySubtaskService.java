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

        boolean isIntersect = TaskableValidator.checkIntersectionClosestSearch(
                subtask,
                repository.getPrioritizedTasksInTree()
        );

        if (isIntersect) {
            remove(subtask.getId());
            throw new IllegalArgumentException("task should not intersect");
        }

        return subtask;
    }

    @Override
    public Subtask update(Subtask subtask) throws NoSuchElementException {
        boolean isIntersect = TaskableValidator.checkIntersectionClosestSearch(
                subtask,
                repository.getPrioritizedTasksInTree()
        );

        if (isIntersect) {
            remove(subtask.getId());
            throw new IllegalArgumentException("task should not intersect");
        }

        return super.update(subtask);
    }
}
