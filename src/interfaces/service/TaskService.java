package interfaces.service;

import java.util.ArrayList;

import dtos.TaskCreationData;
import model.Task;

public interface TaskService {
    Task create(TaskCreationData dto);

    Task get(Integer id);
    ArrayList<Task> getAll();

    Task update(Task task);

    void remove(Integer id);
    void removeAll();
}
