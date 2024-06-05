package service.taskService;

import interfaces.HistoryManager;
import model.Task;
import repository.FileBackedRepository;
import util.IdGenerator;

import java.util.NoSuchElementException;

public class FileBackedTaskService extends InMemoryTaskService {
    protected FileBackedRepository repository;

    public FileBackedTaskService(
            FileBackedRepository repository,
            IdGenerator idGenerator,
            HistoryManager historyManager) {
        super(repository, idGenerator, historyManager);
        this.repository = repository;
    }

    @Override
    public Task create(String name, String description) {
        Task task = super.create(name, description);

        repository.save();

        return task;
    }

    @Override
    public Task create(String name) {
        Task task = super.create(name);

        repository.save();

        return task;
    }


    @Override
    public Task update(Task task) throws NoSuchElementException, IllegalArgumentException {
        Task resultingTask = super.update(task);

        repository.save();

        return resultingTask;
    }

    @Override
    public void remove(Integer id) {
        super.remove(id);
        repository.save();
    }

    @Override
    public void removeAll() {
        super.removeAll();
        repository.save();
    }
}
