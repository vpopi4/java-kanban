package service.epicService;

import java.util.ArrayList;

import interfaces.HistoryManager;
import interfaces.repository.EpicRepository;
import interfaces.repository.SubtaskRepository;
import interfaces.service.EpicService;
import model.*;
import util.IdGenerator;
import util.TaskManagerConfig;
import util.TaskStatus;

public abstract class AbstractEpicService implements EpicService {
    private final EpicRepository epicRepo;
    private final SubtaskRepository subtaskRepo;
    private final IdGenerator idGenerator;
    private final HistoryManager historyManager;

    public AbstractEpicService(TaskManagerConfig config) {
        this.epicRepo = config.getEpicRepository();
        this.subtaskRepo = config.getSubtaskRepository();
        this.idGenerator = config.getIdGenerator();
        this.historyManager = config.getHistoryManager();
    }

    @Override
    public Epic create(EpicCreationData data) {
        Integer id = idGenerator.generateNewId();
        Epic epic = new Epic(id, data);
        epic.setStatus(TaskStatus.NEW);

        return epicRepo.create(epic);
    }

    @Override
    public Epic get(Integer id) {
        Epic epic = epicRepo.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public ArrayList<Epic> getAll() {
        return epicRepo.getAll();
    }

    @Override
    public Epic update(EpicUpdationData data) {
        Epic savedEpic = epicRepo.get(data.getId());

        savedEpic.setName(data.getName());
        savedEpic.setDescription(data.getDescription());

        return epicRepo.update(savedEpic);
    }

    @Override
    public void remove(Integer id) {
        Epic epic = epicRepo.get(id);

        for (Subtask subtask : epic.getSubtasks()) {
            subtaskRepo.remove(subtask.getId());
        }

        epicRepo.remove(id);
    }

    @Override
    public void removeAll() {
        epicRepo.removeAll();
        subtaskRepo.removeAll();
    }

    @Override
    public ArrayList<Subtask> getSubtasks(Integer epicId) {
        Epic epic = epicRepo.get(epicId);
        return epic.getSubtasks();
    }
}
