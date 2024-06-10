package service.epicService;

import interfaces.HistoryManager;
import interfaces.repository.Repository;
import interfaces.service.EpicService;
import model.Epic;
import model.Subtask;
import util.IdGenerator;
import util.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class AbstractEpicService implements EpicService {
    protected final Repository repository;
    protected final IdGenerator idGenerator;
    protected final HistoryManager historyManager;

    public AbstractEpicService(
            Repository repository,
            IdGenerator idGenerator,
            HistoryManager historyManager
    ) {
        this.repository = repository;
        this.idGenerator = idGenerator;
        this.historyManager = historyManager;
    }

    @Override
    public Epic create(String name,
                       String description,
                       Duration duration,
                       LocalDateTime startTime) {
        Integer id = idGenerator.generateNewId();

        Epic epic = new Epic(id);
        epic.setName(name);
        epic.setDescription(description);
        epic.setStatus(TaskStatus.NEW);
        epic.setDuration(duration);
        epic.setStartTime(startTime);

        return repository.create(epic);
    }

    @Override
    public Epic create(String name, String description) {
        return create(name, description, null, null);
    }

    @Override
    public Epic get(Integer id) throws NoSuchElementException {
        Epic epic = repository
                .getEpicById(id)
                .orElseThrow();
        historyManager.add(epic);
        return epic;
    }

    @Override
    public List<Epic> getAll() {
        return repository.getAllEpics();
    }

    @Override
    public Epic update(Epic epic) throws NoSuchElementException, IllegalArgumentException {
        if (epic == null) {
            throw new IllegalArgumentException();
        }

        Epic savedEpic = repository
                .getEpicById(epic.getId())
                .orElseThrow();

        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());

        return repository.update(savedEpic);
    }

    @Override
    public void remove(Integer id) {
        Optional<Epic> epicInRepo = repository.getEpicById(id);

        if (epicInRepo.isEmpty()) {
            return;
        }

        epicInRepo
                .get()
                .getSubtaskIds()
                .forEach(repository::remove);

        repository.remove(id);
    }

    @Override
    public void removeAll() {
        repository.removeAllEpicsAndSubtasks();
    }

    @Override
    public List<Subtask> getSubtasks(Integer epicId) throws NoSuchElementException {
        Epic epic = repository.getEpicById(epicId).orElseThrow();

        return epic.getSubtaskIds().stream()
                .map(repository::getSubtaskById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
