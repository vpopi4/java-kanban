package interfaces.service;

import model.Epic;
import model.EpicCreationData;
import model.EpicUpdationData;
import model.Subtask;

import java.util.ArrayList;

public interface EpicService {
    Epic create(EpicCreationData data);

    Epic get(Integer id);

    ArrayList<Epic> getAll();

    Epic update(EpicUpdationData data);

    void remove(Integer id);

    void removeAll();

    ArrayList<Subtask> getSubtasks(Integer epicId);
}
