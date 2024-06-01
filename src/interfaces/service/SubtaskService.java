package interfaces.service;

import model.Subtask;
import model.TaskCreationData;

import java.util.ArrayList;

public interface SubtaskService {
    Subtask create(TaskCreationData data, Integer epicId);

    Subtask get(Integer id);

    ArrayList<Subtask> getAll();

    Subtask update(Subtask subtask);

    void remove(Integer id);

    void removeAll();
}
