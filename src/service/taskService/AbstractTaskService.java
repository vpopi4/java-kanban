package service.taskService;

import interfaces.HistoryManager;
import interfaces.repository.TaskRepository;
import interfaces.service.TaskService;
import model.Task;
import model.TaskCreationData;
import util.IdGenerator;
import util.TaskManagerConfig;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public abstract class AbstractTaskService implements TaskService {
    private final TaskRepository repository;
    private final IdGenerator idGenerator;
    private final HistoryManager historyManager;

    public AbstractTaskService(TaskManagerConfig config) {
        this.repository = config.getTaskRepository();
        this.idGenerator = config.getIdGenerator();
        this.historyManager = config.getHistoryManager();
    }

    @Override
    public Task create(TaskCreationData data) {
        if (data == null) {
            data = new TaskCreationData(null, null);
        }

        Integer id = idGenerator.generateNewId();
        Task task = new Task(id, data);

        return repository.create(task);
    }

    @Override
    public Task get(Integer id) {
        Task task = repository.get(id);

        if (task == null) {
            throw new NoSuchElementException("task not found");
        }

        historyManager.add(task);
        return task;
    }

    @Override
    public ArrayList<Task> getAll() {
        return repository.getAll();
    }

    @Override
    public Task update(Task task) {
        if (task == null) {
            throw new IllegalArgumentException();
        }

        Task savedTask = repository.get(task.getId());

        if (savedTask == null) {
            throw new NoSuchElementException("task not found");
        }

        savedTask.setName(task.getName());
        savedTask.setDescription(task.getDescription());
        savedTask.setStatus(task.getStatus());

        return repository.update(savedTask);
    }

    @Override
    public void remove(Integer id) {
        repository.remove(id);
    }

    @Override
    public void removeAll() {
        repository.removeAll();
    }
}
