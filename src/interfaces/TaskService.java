package interfaces;

import java.util.ArrayList;

import dtos.TaskDTO;
import model.Task;

public interface TaskService {
    Task create(TaskDTO dto);

    Task get(Integer id);
    ArrayList<Task> getAll();

    Task update(Task task);

    void remove(Integer id);
    void removeAll();
}
