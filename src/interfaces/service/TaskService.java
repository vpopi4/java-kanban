package interfaces.service;

import model.Task;
import model.TaskCreationData;

import java.util.ArrayList;

public interface TaskService {
    Task create(TaskCreationData dto);

    Task get(Integer id);

    ArrayList<Task> getAll();

    Task update(Task task);

    void remove(Integer id);

    void removeAll();
}
