package repository;

import interfaces.model.Taskable;
import interfaces.repository.Repository;
import model.Epic;
import model.Subtask;
import model.Task;
import util.TaskType;

import java.util.*;

public class InMemoryRepository implements Repository {
    protected final Map<Integer, Taskable> store;
    protected final Set<Integer> tasks;
    protected final Set<Integer> epics;
    protected final Set<Integer> subtasks;
    private final TreeSet<Taskable> prioritizedTasks;

    public InMemoryRepository() {
        store = new LinkedHashMap<>();
        tasks = new HashSet<>();
        epics = new HashSet<>();
        subtasks = new HashSet<>();
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Taskable::getStartTime));
    }

    @Override
    public Task create(Task task) {
        store.put(task.getId(), task);
        tasks.add(task.getId());
        addToPrioritizedTasks(task);
        return task;
    }

    @Override
    public Epic create(Epic epic) {
        store.put(epic.getId(), epic);
        epics.add(epic.getId());
        return epic;
    }

    @Override
    public Subtask create(Subtask subtask) {
        store.put(subtask.getId(), subtask);
        subtasks.add(subtask.getId());
        addToPrioritizedTasks(subtask);
        return subtask;
    }

    @Override
    public Optional<Taskable> getAnyTaskById(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Task> getTaskById(Integer id) {
        if (!tasks.contains(id)) {
            return Optional.empty();
        }

        return Optional.ofNullable((Task) store.get(id));
    }

    @Override
    public Optional<Epic> getEpicById(Integer id) {
        if (!epics.contains(id)) {
            return Optional.empty();
        }

        return Optional.ofNullable((Epic) store.get(id));
    }

    @Override
    public Optional<Subtask> getSubtaskById(Integer id) {
        if (!subtasks.contains(id)) {
            return Optional.empty();
        }

        return Optional.ofNullable((Subtask) store.get(id));
    }

    @Override
    public List<Task> getAllTasks() {
        return tasks.stream()
                .map(store::get)
                .filter(taskable -> taskable.getType() == TaskType.TASK)
                .map(taskable -> (Task) taskable)
                .toList();
    }

    @Override
    public List<Epic> getAllEpics() {
        return epics.stream()
                .map(store::get)
                .filter(taskable -> taskable.getType() == TaskType.EPIC)
                .map(taskable -> (Epic) taskable)
                .toList();
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return subtasks.stream()
                .map(store::get)
                .filter(taskable -> taskable.getType() == TaskType.SUBTASK)
                .map(taskable -> (Subtask) taskable)
                .toList();
    }

    @Override
    public List<Taskable> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public TreeSet<Taskable> getPrioritizedTasksInTree() {
        return prioritizedTasks;
    }

    @Override
    public Task update(Task task) throws NoSuchElementException {
        int id = task.getId();

        if (!tasks.contains(id) || !store.containsKey(id)) {
            throw new NoSuchElementException("task not found");
        }

        store.put(id, task);

        updateToPrioritizedTasks(task);

        return task;
    }

    @Override
    public Epic update(Epic epic) throws NoSuchElementException {
        int id = epic.getId();

        if (!epics.contains(id) || !store.containsKey(id)) {
            throw new NoSuchElementException("epic not found");
        }

        store.put(id, epic);

        return epic;
    }

    @Override
    public Subtask update(Subtask subtask) throws NoSuchElementException {
        int id = subtask.getId();

        if (!subtasks.contains(id) || !store.containsKey(id)) {
            throw new NoSuchElementException("subtask not found");
        }

        store.put(id, subtask);
        updateToPrioritizedTasks(subtask);

        return subtask;
    }

    @Override
    public void remove(Integer id) {
        if (tasks.contains(id)) {
            Taskable taskable = store.get(id);
            prioritizedTasks.remove(taskable);
            tasks.remove(id);
        } else if (epics.contains(id)) {
            epics.remove(id);
        } else {
            Taskable taskable = store.get(id);
            prioritizedTasks.remove(taskable);
            subtasks.remove(id);
        }
        store.remove(id);
    }

    @Override
    public void removeAllTasks() {
        for (Integer id : tasks) {
            Taskable taskable = store.get(id);
            prioritizedTasks.remove(taskable);
            store.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpicsAndSubtasks() {
        removeAllSubtasks();

        for (Integer id : epics) {
            store.remove(id);
        }
        epics.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Integer id : subtasks) {
            Taskable taskable = store.get(id);
            prioritizedTasks.remove(taskable);
            store.remove(id);
        }
        subtasks.clear();
    }

    private void addToPrioritizedTasks(Taskable taskable) {
        if (taskable.getStartTime() != null) {
            prioritizedTasks.add(taskable);
        }
    }

    private void updateToPrioritizedTasks(Taskable taskable) {
        if (taskable.getStartTime() != null) {
            prioritizedTasks.remove(taskable);
            prioritizedTasks.add(taskable);
        }
    }
}
