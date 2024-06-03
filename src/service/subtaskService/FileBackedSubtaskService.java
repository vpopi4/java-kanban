package service.subtaskService;

import interfaces.HistoryManager;
import model.Subtask;
import repository.FileBackedRepository;
import util.IdGenerator;

import java.util.NoSuchElementException;

public class FileBackedSubtaskService extends InMemorySubtaskService {
    protected FileBackedRepository repository;

    public FileBackedSubtaskService(
            FileBackedRepository repository,
            IdGenerator idGenerator,
            HistoryManager historyManager) {
        super(repository, idGenerator, historyManager);
    }

    @Override
    public Subtask create(Integer epicId, String name, String description) {
        Subtask subtask = super.create(epicId, name, description);

        repository.save();

        return subtask;
    }

    @Override
    public Subtask create(Integer epicId, String name) {
        Subtask subtask = super.create(epicId, name);

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
