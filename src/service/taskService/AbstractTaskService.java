package service.taskService;

import dtos.TaskCreationData;
import interfaces.repository.TaskRepository;
import interfaces.HistoryManager;
import interfaces.TaskService;
import model.Task;
import util.IdGenerator;

import java.util.ArrayList;

public abstract class AbstractTaskService implements TaskService {
    private final TaskRepository repository;
    private final IdGenerator idGenerator;
    private final HistoryManager<Task> historyService;

    public AbstractTaskService(
            TaskRepository repository,
            IdGenerator idGenerator,
            HistoryManager<Task> historyService
    ) {
        this.repository = repository;
        this.idGenerator = idGenerator;
        this.historyService = historyService;
    }

    @Override
    public Task create(TaskCreationData data) {
        Task task = new Task(idGenerator.generateNewId(), data);

        return repository.create(task);
    }

    @Override
    public Task get(Integer id) {
        Task task = repository.get(id);
        historyService.add(task);
        return task;
    }

    @Override
    public ArrayList<Task> getAll() {
        return repository.getAll();
    }

    @Override
    public Task update(Task task) {
        Task savedTask = repository.get(task.getId());

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
