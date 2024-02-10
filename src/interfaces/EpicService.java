package interfaces;

import java.util.ArrayList;

import dtos.EpicUpdationData;
import dtos.TaskCreationData;
import model.Epic;
import model.Subtask;

public interface EpicService {
    Epic create(TaskCreationData data);

    Epic get(Integer id);
    ArrayList<Epic> getAll();

    Epic update(EpicUpdationData data);

    void remove(Integer id);
    void removeAll();

    ArrayList<Subtask> getSubtasks(Integer epicId);
}
