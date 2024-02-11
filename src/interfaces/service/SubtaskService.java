package interfaces.service;

import java.util.ArrayList;

import dtos.TaskCreationData;
import model.Subtask;

public interface SubtaskService {
    Subtask create( TaskCreationData data, Integer epicId);

    Subtask get(Integer id);
    ArrayList<Subtask> getAll();

    Subtask update(Subtask subtask);

    void remove(Integer id);
    void removeAll();
}
