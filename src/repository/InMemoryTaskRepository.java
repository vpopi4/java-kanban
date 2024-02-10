package repository;

import interfaces.repository.TaskRepository;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskRepository implements TaskRepository {
    private HashMap<Integer, Task> store;

    public InMemoryTaskRepository() {
        store = new HashMap<>();
    }

    @Override
    public Task create(Task task) {
        store.put(task.getId(), task);
        return task;
    }

    @Override
    public Task get(Integer id) {
        return store.get(id);
    }

    @Override
    public ArrayList<Task> getAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Task update(Task task) {
        store.put(task.getId(), task);
        return task;
    }

    @Override
    public void remove(Integer id) {
        store.remove(id);
    }

    @Override
    public void removeAll() {
        store.clear();
    }
}
