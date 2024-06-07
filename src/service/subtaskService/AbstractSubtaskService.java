package service.subtaskService;

import interfaces.HistoryManager;
import interfaces.repository.Repository;
import interfaces.service.SubtaskService;
import model.Epic;
import model.Subtask;
import util.IdGenerator;
import util.TaskStatus;

import java.util.List;
import java.util.NoSuchElementException;

public abstract class AbstractSubtaskService implements SubtaskService {
    private final Repository repository;
    private final IdGenerator idGenerator;
    private final HistoryManager historyManager;

    public AbstractSubtaskService(
            Repository repository,
            IdGenerator idGenerator,
            HistoryManager historyManager
    ) {
        this.repository = repository;
        this.idGenerator = idGenerator;
        this.historyManager = historyManager;
    }

    @Override
    public Subtask create(Integer epicId, String name, String description) throws NoSuchElementException {
        Epic epic = repository.getEpicById(epicId);
        Integer id = idGenerator.generateNewId();

        Subtask subtask = new Subtask(id, epic.getId());
        subtask.setName(name);
        subtask.setDescription(description);

        epic.addSubtaskId(id);

        repository.update(epic);
        return repository.create(subtask);
    }

    @Override
    public Subtask create(Integer epicId, String name) throws NoSuchElementException {
        Epic epic = repository.getEpicById(epicId);
        Integer id = idGenerator.generateNewId();

        Subtask subtask = new Subtask(id, epic.getId());
        subtask.setName(name);
        subtask.setDescription("");

        epic.addSubtaskId(id);

        repository.update(epic);
        return repository.create(subtask);
    }

    @Override
    public Subtask get(Integer id) throws NoSuchElementException {
        Subtask subtask = repository.getSubtaskById(id);

        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public List<Subtask> getAll() {
        return repository.getAllSubtasks();
    }

    @Override
    public Subtask update(Subtask subtask) throws NoSuchElementException {
        Subtask savedSubtask = repository.getSubtaskById(subtask.getId());
        Epic savedEpic = repository.getEpicById(subtask.getEpicId());

        savedSubtask.setName(subtask.getName());
        savedSubtask.setDescription(subtask.getDescription());
        savedSubtask.setStatus(subtask.getStatus());

        savedEpic.setStatus(calculateStatusOfEpic(savedEpic));

        repository.update(savedEpic);
        return repository.update(savedSubtask);
    }

    @Override
    public void remove(Integer id) {
        Subtask subtask = repository.getSubtaskById(id);
        Epic epic = repository.getEpicById(subtask.getEpicId());

        epic.getSubtaskIds().remove(id);
        epic.setStatus(calculateStatusOfEpic(epic));

        repository.update(epic);
        repository.remove(id);
    }

    @Override
    public void removeAll() {
        repository.removeAllSubtasks();

        for (Epic epic : repository.getAllEpics()) {
            epic.getSubtaskIds().clear();
            epic.setStatus(TaskStatus.NEW);
        }
    }

    private TaskStatus calculateStatusOfEpic(Epic epic) {
        int size = epic.getSubtaskIds().size();

        if (size == 0) {
            return TaskStatus.NEW;
        }

        int countOfNew = 0;
        int countOfDone = 0;

        for (Integer id : epic.getSubtaskIds()) {
            Subtask subtask = repository.getSubtaskById(id);
            if (subtask.getStatus() == TaskStatus.NEW) {
                countOfNew++;
            } else if (subtask.getStatus() == TaskStatus.DONE) {
                countOfDone++;
            }
        }

        if (countOfNew == size) {
            return TaskStatus.NEW;
        } else if (countOfDone == size) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }
}
