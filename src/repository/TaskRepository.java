package repository;

import model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskRepository {
    private final HashMap<Integer, Task> tasks;

    public TaskRepository() {
        tasks = new HashMap<>();
    }

    public ArrayList<Task> getAll() {
        Collection<Task> collection = tasks.values();
        return new ArrayList<>(collection);
    }

    public Task getById(int id) throws IllegalArgumentException {
        if (!tasks.containsKey(id)) {
            throw new IllegalArgumentException("No such task");
        }
        return tasks.get(id);
    }

    public void removeALl() {
        tasks.clear();
    }

    public void removeById(int id) {
        tasks.remove(id);
    }

    public void create(Task task) throws IllegalArgumentException, NullPointerException {
        if (task == null) {
            throw new NullPointerException();
        }

        if (tasks.containsKey(task.getId())) {
            throw new IllegalArgumentException("This id is already in use");
        }

        tasks.put(task.getId(), task);
    }

    public void update(Task task) throws IllegalArgumentException, NullPointerException {
        if (task == null) {
            throw new NullPointerException();
        }

        if (!tasks.containsKey(task.getId())) {
            throw new IllegalArgumentException("No such task");
        }

        tasks.put(task.getId(), task);
    }
}
