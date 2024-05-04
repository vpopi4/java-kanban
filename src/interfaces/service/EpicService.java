package interfaces.service;

import java.util.ArrayList;

import model.*;

public interface EpicService {
    Epic create(EpicCreationData data);

    Epic get(Integer id);
    ArrayList<Epic> getAll();

    Epic update(EpicUpdationData data);

    void remove(Integer id);
    void removeAll();

    ArrayList<Subtask> getSubtasks(Integer epicId);
}
