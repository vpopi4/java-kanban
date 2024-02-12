package interfaces.service;

import java.util.ArrayList;

import model.Task;
import model.TaskCreationData;

public interface TaskService {
    Task create(TaskCreationData dto);

    Task get(Integer id);
    ArrayList<Task> getAll();

    Task update(Task task);

    void remove(Integer id);
    void removeAll();
}
