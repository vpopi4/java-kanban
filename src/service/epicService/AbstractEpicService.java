package service.epicService;

import interfaces.HistoryManager;
import interfaces.repository.Repository;
import interfaces.service.EpicService;
import model.Epic;
import model.Subtask;
import util.IdGenerator;
import util.TaskManagerConfig;
import util.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class AbstractEpicService implements EpicService {
    private final Repository repository;
    private final IdGenerator idGenerator;
    private final HistoryManager historyManager;

    public AbstractEpicService(TaskManagerConfig config) {
        this.repository = config.repository();
        this.idGenerator = config.idGenerator();
        this.historyManager = config.historyManager();
    }

    @Override
    public Epic create(String name, String description) {
        Integer id = idGenerator.generateNewId();

        Epic epic = new Epic(id);
        epic.setName(name);
        epic.setDescription(description);
        epic.setStatus(TaskStatus.NEW);

        return repository.create(epic);
    }

    @Override
    public Epic create(String name) {
        Integer id = idGenerator.generateNewId();

        Epic epic = new Epic(id);
        epic.setName(name);
        epic.setDescription("");
        epic.setStatus(TaskStatus.NEW);

        return repository.create(epic);
    }

    @Override
    public Epic get(Integer id) throws NoSuchElementException {
        Epic epic = repository.getEpicById(id);

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

        Epic savedEpic = repository.getEpicById(epic.getId());

        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());

        return repository.update(savedEpic);
    }

    @Override
    public void remove(Integer id) {
        try {
            Epic epic = repository.getEpicById(id);

            for (Integer subtaskId : epic.getSubtaskIds()) {
                repository.remove(subtaskId);
            }

            repository.remove(id);
        } catch (NoSuchElementException e) {
            // pass
        }
    }

    @Override
    public void removeAll() {
        repository.removeAllEpicsAndSubtasks();
    }

    @Override
    public List<Subtask> getSubtasks(Integer epicId) throws NoSuchElementException {
        Epic epic = repository.getEpicById(epicId);

        List<Subtask> list = new ArrayList<>(epic.getSubtaskIds().size());

        for (Integer subtaskId : epic.getSubtaskIds()) {
            list.add(repository.getSubtaskById(subtaskId));
        }

        return list;
    }
}
