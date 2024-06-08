package service.subtaskService;

import interfaces.HistoryManager;
import model.Subtask;
import repository.FileBackedRepository;
import util.IdGenerator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class FileBackedSubtaskService extends InMemorySubtaskService {
    protected FileBackedRepository repository;

    public FileBackedSubtaskService(
            FileBackedRepository repository,
            IdGenerator idGenerator,
            HistoryManager historyManager) {
        super(repository, idGenerator, historyManager);
        this.repository = repository;
    }

    @Override
    public Subtask create(Integer epicId, String name, String description, Duration duration, LocalDateTime startTime) {
        Subtask subtask = super.create(epicId, name, description, duration, startTime);

        repository.save();

        return subtask;
    }

    @Override
    public Subtask create(Integer epicId, String name, String description) {
        Subtask subtask = super.create(epicId, name, description);

        repository.save();

        return subtask;
    }

    @Override
    public Subtask update(Subtask subtask) throws NoSuchElementException, IllegalArgumentException {
        Subtask resultingSubtask = super.update(subtask);

        repository.save();

        return resultingSubtask;
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
