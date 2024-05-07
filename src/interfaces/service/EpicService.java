package interfaces.service;

import model.Epic;
import model.Subtask;
import model.TaskCreationData;

import java.util.ArrayList;

public interface EpicService {
    Epic create(TaskCreationData data);

    Epic get(Integer id);

    ArrayList<Epic> getAll();

    Epic update(Integer id, TaskCreationData data);

    void remove(Integer id);

    void removeAll();

    ArrayList<Subtask> getSubtasks(Integer epicId);
}
