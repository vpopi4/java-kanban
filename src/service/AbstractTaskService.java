package service;

import dtos.TaskDTO;
import interfaces.repository.TaskRepository;
import interfaces.TaskService;
import model.Task;

import java.util.ArrayList;

public abstract class AbstractTaskService implements TaskService {
    private final TaskRepository repository;
    private final IdGenerator idGenerator;

    public AbstractTaskService(TaskRepository repository, IdGenerator idGenerator) {
        this.repository = repository;
        this.idGenerator = idGenerator;
    }

    @Override
    public Task create(TaskDTO dto) {
        Task task = new Task(idGenerator.generateNewId(), dto);

        repository.create(task);

        return task;
    }

    @Override
    public Task get(Integer id) {
        return repository.get(id);
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