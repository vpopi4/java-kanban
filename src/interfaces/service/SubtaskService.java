package interfaces.service;

import model.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public interface SubtaskService {
    Subtask create(Integer epicId,
                   String name,
                   String description,
                   Duration duration,
                   LocalDateTime startTime);

    Subtask create(Integer epicId, String name, String description);

    Subtask get(Integer id) throws NoSuchElementException;

    List<Subtask> getAll();

    Subtask update(Subtask subtask) throws NoSuchElementException, IllegalArgumentException;

    void remove(Integer id);

    void removeAll();
}
