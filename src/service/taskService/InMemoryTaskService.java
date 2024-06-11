package service.taskService;

import interfaces.HistoryManager;
import model.Task;
import repository.InMemoryRepository;
import util.IdGenerator;
import util.TaskableValidator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class InMemoryTaskService extends AbstractTaskService {
    protected final InMemoryRepository repository;

    public InMemoryTaskService(
            InMemoryRepository repository,
            IdGenerator idGenerator,
            HistoryManager historyManager
    ) {
        super(repository, idGenerator, historyManager);
        this.repository = repository;
    }

    @Override
    public Task create(String name, String description, Duration duration, LocalDateTime startTime) {
        Task task = super.create(name, description, duration, startTime);


        try {
            TaskableValidator.checkIntersectionClosestSearch(
                    task,
                    repository.getPrioritizedTasksInTree()
            );
        } catch (TaskableValidator.IntersectionException e) {
            remove(task.getId());
            throw e;
        }


        return task;
    }

    @Override
    public Task update(Task task) throws NoSuchElementException, IllegalArgumentException {
        try {
            TaskableValidator.checkIntersectionClosestSearch(
                    task,
                    repository.getPrioritizedTasksInTree()
            );
        } catch (TaskableValidator.IntersectionException e) {
            remove(task.getId());
            throw e;
        }


        return super.update(task);
    }
}
