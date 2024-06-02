package service.taskService;

import interfaces.HistoryManager;
import interfaces.repository.Repository;
import interfaces.service.TaskService;
import model.Task;
import util.IdGenerator;
import util.TaskManagerConfig;

import java.util.List;
import java.util.NoSuchElementException;

public abstract class AbstractTaskService implements TaskService {
    private final Repository repository;
    private final IdGenerator idGenerator;
    private final HistoryManager historyManager;

    public AbstractTaskService(TaskManagerConfig config) {
        this.repository = config.repository();
        this.idGenerator = config.idGenerator();
        this.historyManager = config.historyManager();
    }

    @Override
    public Task create(String name, String description) {
        Integer id = idGenerator.generateNewId();
        Task task = new Task(id);
        task.setName(name);
        task.setDescription(description);

        return repository.create(task);
    }

    @Override
    public Task create(String name) {
        Integer id = idGenerator.generateNewId();
        Task task = new Task(id);
        task.setName(name);
        task.setDescription("");

        return repository.create(task);
    }

    @Override
    public Task get(Integer id) throws NoSuchElementException {
        Task task = repository.getTaskById(id);
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
