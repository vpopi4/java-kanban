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
import java.util.Optional;

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
                          LocalDateTime startTime) throws NoSuchElementException {
        Epic epic = repository
                .getEpicById(epicId)
                .orElseThrow();
        Integer id = idGenerator.generateNewId();

        Subtask subtask = new Subtask(id, epic.getId());
        subtask.setName(name);
        subtask.setDescription(description);
        subtask.setStatus(TaskStatus.NEW);
        subtask.setDuration(duration);
        subtask.setStartTime(startTime);

        epic.addSubtaskId(id);
        epic.setStartTime(calculateStartTime(epic));
        epic.setDuration(calculateDuration(epic));

        repository.update(epic);
        return repository.create(subtask);
    }

    @Override
    public Subtask create(Integer epicId, String name, String description) throws NoSuchElementException {
        return create(epicId, name, description, null, null);
    }

    @Override
    public Subtask get(Integer id) throws NoSuchElementException {
        Subtask subtask = repository
                .getSubtaskById(id)
                .orElseThrow();
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public List<Subtask> getAll() {
        return repository.getAllSubtasks();
    }

    @Override
    public Subtask update(Subtask subtask) throws NoSuchElementException {
        if (subtask == null) {
            throw new IllegalArgumentException();
        }

        Subtask savedSubtask = repository
                .getSubtaskById(subtask.getId())
                .orElseThrow();
        Epic savedEpic = repository
                .getEpicById(subtask.getEpicId())
                .orElseThrow();

        if (subtask != savedSubtask) {
            savedSubtask.setName(subtask.getName());
            savedSubtask.setDescription(subtask.getDescription());
            savedSubtask.setStatus(subtask.getStatus());
            savedSubtask.setStartTime(subtask.getStartTime());
            savedSubtask.setDuration(subtask.getDuration());
        }

        savedEpic.setStatus(calculateStatus(savedEpic));
        savedEpic.setStartTime(calculateStartTime(savedEpic));
        savedEpic.setDuration(calculateDuration(savedEpic));

        repository.update(savedEpic);
        repository.update(savedSubtask);

        return savedSubtask;
    }

    @Override
    public void remove(Integer id) {
        Optional<Subtask> subtaskInRepo = repository.getSubtaskById(id);

        if (subtaskInRepo.isEmpty()) {
            return;
        }

        Integer epicId = subtaskInRepo.get().getEpicId();
        Optional<Epic> epicInRepo = repository.getEpicById(epicId);

        if (epicInRepo.isPresent()) {
            Epic epic = epicInRepo.get();

            epic.getSubtaskIds().remove(id);

            epic.setStatus(calculateStatus(epic));
            epic.setStartTime(calculateStartTime(epic));
            epic.setDuration(calculateDuration(epic));

            repository.update(epic);
        }

        repository.remove(id);
    }

    @Override
    public void removeAll() {
        repository.removeAllSubtasks();

        for (Epic epic : repository.getAllEpics()) {
            epic.getSubtaskIds().clear();
            epic.setStatus(TaskStatus.NEW);
            epic.setDuration(Duration.ZERO);
            epic.setStartTime(null);
        }
    }

    private LocalDateTime calculateStartTime(Epic epic) {
        return epic.getSubtaskIds().stream()
                .map(repository::getSubtaskById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Task::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    private Duration calculateDuration(Epic epic) {
        return epic.getSubtaskIds().stream()
                .map(repository::getSubtaskById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
    }

    private TaskStatus calculateStatus(Epic epic) {
        List<Subtask> subtasks = epic.getSubtaskIds().stream()
                .map(repository::getSubtaskById)
                .filter(Optional::isPresent)
                .map(Optional::get)
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
}
