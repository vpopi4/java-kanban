package interfaces.service;

import model.Epic;
import model.Subtask;

import java.util.List;
import java.util.NoSuchElementException;

public interface EpicService {
    Epic create(String name, String description);

    Epic create(String name);

    Epic get(Integer id) throws NoSuchElementException;

    List<Epic> getAll();

    Epic update(Epic epic) throws NoSuchElementException, IllegalArgumentException;

    void remove(Integer id);

    void removeAll();

    List<Subtask> getSubtasks(Integer epicId) throws NoSuchElementException;
}
