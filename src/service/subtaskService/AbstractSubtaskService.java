package service.subtaskService;

import interfaces.HistoryManager;
import interfaces.repository.Repository;
import interfaces.service.SubtaskService;
import model.Epic;
import model.Subtask;
import model.Task;
import util.IdGenerator;
import util.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class AbstractSubtaskService implements SubtaskService {
    private final Repository repository;
    private final IdGenerator idGenerator;
    private final HistoryManager historyManager;

    public AbstractSubtaskService(Repository repository, IdGenerator idGenerator, HistoryManager historyManager) {
        this.repository = repository;
        this.idGenerator = idGenerator;
        this.historyManager = historyManager;
    }

    @Override
    public Subtask create(Integer epicId,
                          String name,
                          String description,
                          Duration duration,
                          LocalDateTime startTime) {
        Epic epic = repository.getEpicById(epicId);
        Integer id = idGenerator.generateNewId();

        Subtask subtask = new Subtask(id, epic.getId());
        subtask.setName(name);
        subtask.setDescription(description);
        subtask.setStatus(TaskStatus.NEW);
        subtask.setDuration(duration);
        subtask.setStartTime(startTime);

        epic.addSubtaskId(id);

        repository.update(epic);
        return repository.create(subtask);
    }

    @Override
    public Subtask create(Integer epicId, String name, String description) throws NoSuchElementException {
        return create(epicId, name, description, null, null);
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

        savedEpic.setStatus(calculateStatus(savedEpic));

        repository.update(savedEpic);
        return repository.update(savedSubtask);
    }

    @Override
    public void remove(Integer id) {
        Subtask subtask = repository.getSubtaskById(id);
        Epic epic = repository.getEpicById(subtask.getEpicId());

        epic.getSubtaskIds().remove(id);
        epic.setStatus(calculateStatus(epic));

        repository.update(epic);
        repository.remove(id);
    }

    @Override
    public void removeAll() {
        repository.removeAllSubtasks();

        for (Epic epic : repository.getAllEpics()) {
            epic.getSubtaskIds().clear();
            epic.setStatus(TaskStatus.NEW);
            epic.setDuration(Duration.ZERO);
        }
    }

    private LocalDateTime calculateStartTime(Epic epic) {
        return epic.getSubtaskIds().stream()
                .map(repository::getSubtaskById)
                .map(Task::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    private Duration calculateDuration(Epic epic) {
        return epic.getSubtaskIds().stream()
                .map(repository::getSubtaskById)
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
    }

    private TaskStatus calculateStatus(Epic epic) {
        List<Subtask> subtasks = epic.getSubtaskIds().stream()
                .map(repository::getSubtaskById)
                .toList();

        long countOfNew = subtasks.stream()
                .filter(subtask -> subtask.getStatus() == TaskStatus.NEW)
                .count();

        long countOfDone = subtasks.stream()
                .filter(subtask -> subtask.getStatus() == TaskStatus.DONE)
                .count();

        long size = subtasks.size();

        if (size == 0 || countOfNew == size) {
            return TaskStatus.NEW;
        } else if (countOfDone == size) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }

    private Stream<Subtask> getSubtasksOfEpic(Epic epic) {
        return epic.getSubtaskIds().stream()
                .map(repository::getSubtaskById);
    }
}
