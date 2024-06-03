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
    private final Set<Integer> tasks;
    private final Set<Integer> epics;
    private final Set<Integer> subtasks;

    public InMemoryRepository() {
        store = new HashMap<>();
        tasks = new HashSet<>();
        epics = new HashSet<>();
        subtasks = new HashSet<>();
    }

    @Override
    public Task create(Task task) {
        store.put(task.getId(), task);
        tasks.add(task.getId());
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
        return subtask;
    }

    @Override
    public Task getTaskById(Integer id) throws NoSuchElementException {
        if (!tasks.contains(id) || !store.containsKey(id)) {
            throw new NoSuchElementException("task not found");
        }

        Taskable record = store.get(id);

        if (record.getType() != TaskType.TASK) {
            tasks.remove(id);
            throw new IllegalArgumentException("record is not a task");
        }

        return (Task) record;
    }

    @Override
    public Epic getEpicById(Integer id) throws NoSuchElementException {
        if (!epics.contains(id) || !store.containsKey(id)) {
            throw new NoSuchElementException("epic not found");
        }

        Taskable record = store.get(id);

        if (record.getType() != TaskType.EPIC) {
            epics.remove(id);
            throw new IllegalArgumentException("record is not an epic");
        }

        return (Epic) record;
    }

    @Override
    public Subtask getSubtaskById(Integer id) throws NoSuchElementException {
        if (!subtasks.contains(id) || !store.containsKey(id)) {
            throw new NoSuchElementException("subtask not found");
        }

        Taskable record = store.get(id);

        if (record.getType() != TaskType.SUBTASK) {
            subtasks.remove(id);
            throw new IllegalArgumentException("record is not a subtask");
        }

        return (Subtask) record;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> list = new ArrayList<>(tasks.size());
        for (Integer id : tasks) {
            Taskable record = store.get(id);

            if (record.getType() != TaskType.TASK) {
                tasks.remove(id);
                continue;
            }

            list.add((Task) record);
        }
        return list;
    }

    @Override
    public List<Epic> getAllEpics() {
        List<Epic> list = new ArrayList<>(epics.size());
        for (Integer id : epics) {
            Taskable record = store.get(id);

            if (record.getType() != TaskType.EPIC) {
                epics.remove(id);
                continue;
            }

            list.add((Epic) record);
        }
        return list;
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        List<Subtask> list = new ArrayList<>(subtasks.size());
        for (Integer id : subtasks) {
            Taskable record = store.get(id);

            if (record.getType() != TaskType.SUBTASK) {
                epics.remove(id);
                continue;
            }

            list.add((Subtask) record);
        }
        return list;
    }

    @Override
    public Task update(Task task) throws NoSuchElementException {
        int id = task.getId();

        if (!tasks.contains(id) || !store.containsKey(id)) {
            throw new NoSuchElementException("task not found");
        }

        store.put(id, task);

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

        return subtask;
    }

    @Override
    public void remove(Integer id) {
        if (tasks.contains(id)) {
            tasks.remove(id);
        } else if (epics.contains(id)) {
            epics.remove(id);
        } else {
            subtasks.remove(id);
        }
        store.remove(id);
    }

    @Override
    public void removeAllTasks() {
        for (Integer id : tasks) {
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
            store.remove(id);
        }
        subtasks.clear();
    }
}
