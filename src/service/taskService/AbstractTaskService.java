package service.taskService;

import interfaces.HistoryManager;
import interfaces.repository.Repository;
import interfaces.service.TaskService;
import model.Task;
import util.IdGenerator;
import util.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class AbstractTaskService implements TaskService {
    private final Repository repository;
    private final IdGenerator idGenerator;
    private final HistoryManager historyManager;

    public AbstractTaskService(
            Repository repository,
            IdGenerator idGenerator,
            HistoryManager historyManager
    ) {
        this.repository = repository;
        this.idGenerator = idGenerator;
        this.historyManager = historyManager;
    }

    @Override
    public Task create(
            String name,
            String description,
            Duration duration,
            LocalDateTime startTime
    ) {
        Integer id = idGenerator.generateNewId();

        Task task = new Task(id);
        task.setName(name);
        task.setDescription(description);
        task.setStatus(TaskStatus.NEW);
        task.setDuration(duration);
        task.setStartTime(startTime);

        return repository.create(task);
    }

    @Override
    public Task create(String name, String description) {
        return create(name, description, null, null);
    }

    @Override
    public Task get(Integer id) throws NoSuchElementException {
        Task task = repository
                .getTaskById(id)
                .orElseThrow();
        historyManager.add(task);
        return task;
    }

    @Override
    public List<Task> getAll() {
        return repository.getAllTasks();
    }

    @Override
    public Task update(Task task) throws NoSuchElementException, IllegalArgumentException {
        if (task == null) {
            throw new IllegalArgumentException();
        }

        Task savedTask = repository
                .getTaskById(task.getId())
                .orElseThrow();

        if (task != savedTask) {
            savedTask.setName(task.getName());
            savedTask.setDescription(task.getDescription());
            savedTask.setStatus(task.getStatus());
            savedTask.setStartTime(task.getStartTime());
            savedTask.setDuration(task.getDuration());
        }

        return repository.update(task);
    }

    @Override
    public void remove(Integer id) {
        repository.remove(id);
    }

    @Override
    public void removeAll() {
        repository.removeAllTasks();
    }
}
