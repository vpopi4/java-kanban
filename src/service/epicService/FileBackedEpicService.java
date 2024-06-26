package service.epicService;

import interfaces.HistoryManager;
import model.Epic;
import repository.FileBackedRepository;
import util.IdGenerator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class FileBackedEpicService extends InMemoryEpicService {
    protected FileBackedRepository repository;

    public FileBackedEpicService(
            FileBackedRepository repository,
            IdGenerator idGenerator,
            HistoryManager historyManager) {
        super(repository, idGenerator, historyManager);
        this.repository = repository;
    }

    @Override
    public Epic create(String name, String description, Duration duration, LocalDateTime startTime) {
        Epic epic = super.create(name, description, duration, startTime);

        repository.save();

        return epic;
    }

    @Override
    public Epic create(String name, String description) {
        Epic epic = super.create(name, description);

        repository.save();

        return epic;
    }

    @Override
    public Epic update(Epic epic) throws NoSuchElementException, IllegalArgumentException {
        Epic resultingEpic = super.update(epic);

        repository.save();

        return resultingEpic;
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
