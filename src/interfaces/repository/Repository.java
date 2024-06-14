package interfaces.repository;

import interfaces.model.Taskable;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;
import java.util.Optional;

public interface Repository {
    Task create(Task task);

    Epic create(Epic task);

    Subtask create(Subtask task);

    Optional<Taskable> getAnyTaskById(Integer id);

    Optional<Task> getTaskById(Integer id);

    Optional<Epic> getEpicById(Integer id);

    Optional<Subtask> getSubtaskById(Integer id);

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
