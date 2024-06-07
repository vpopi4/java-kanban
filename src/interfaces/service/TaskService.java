package interfaces.service;

import model.Task;

import java.util.List;
import java.util.NoSuchElementException;

public interface TaskService {
    Task create(String name, String description);

    Task create(String name);

    Task get(Integer id) throws NoSuchElementException;

    List<Task> getAll();

    Task update(Task task) throws NoSuchElementException, IllegalArgumentException;

    void remove(Integer id);

    void removeAll();
}
