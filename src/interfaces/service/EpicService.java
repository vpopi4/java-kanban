package interfaces.service;

import java.util.ArrayList;

import model.Epic;
import model.EpicUpdationData;
import model.Subtask;
import model.TaskCreationData;

public interface EpicService {
    Epic create(TaskCreationData data);

    Epic get(Integer id);
    ArrayList<Epic> getAll();

    Epic update(EpicUpdationData data);

    void remove(Integer id);
    void removeAll();

    ArrayList<Subtask> getSubtasks(Integer epicId);
}
