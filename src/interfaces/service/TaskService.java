package interfaces.service;

import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public interface TaskService {
    Task create(String name,
                String description,
                Duration duration,
                LocalDateTime startTime);

    Task create(String name, String description);

    Task get(Integer id) throws NoSuchElementException;

    List<Task> getAll();

    Task update(Task task) throws NoSuchElementException, IllegalArgumentException;

    void remove(Integer id);

    void removeAll();
}
