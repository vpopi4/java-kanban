package interfaces.repository;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;
import java.util.NoSuchElementException;

public interface Repository {
    Task create(Task task);

    Epic create(Epic task);

    Subtask create(Subtask task);

    Task getTaskById(Integer id) throws NoSuchElementException;

    Epic getEpicById(Integer id) throws NoSuchElementException;

    Subtask getSubtaskById(Integer id) throws NoSuchElementException;

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    Task update(Task task);

    Epic update(Epic epic);

    Subtask update(Subtask subtask);

    void remove(Integer id);

    void removeAllTasks();

    void removeAllEpicsAndSubtasks();

    void removeAllSubtasks();
}
