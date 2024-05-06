package service.epicService;

import interfaces.HistoryManager;
import interfaces.repository.EpicRepository;
import interfaces.repository.SubtaskRepository;
import interfaces.service.EpicService;
import model.Epic;
import model.EpicCreationData;
import model.EpicUpdationData;
import model.Subtask;
import util.IdGenerator;
import util.TaskManagerConfig;
import util.TaskStatus;

import java.util.ArrayList;
import java.util.NoSuchElementException;

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
        if (data == null) {
            data = new EpicCreationData(null, null);
        }

        Integer id = idGenerator.generateNewId();
        Epic epic = new Epic(id, data);
        epic.setStatus(TaskStatus.NEW);

        return epicRepo.create(epic);
    }

    @Override
    public Epic get(Integer id) {
        Epic epic = epicRepo.get(id);

        if (epic == null) {
            throw new NoSuchElementException("epic not found");
        }

        historyManager.add(epic);
        return epic;
    }

    @Override
    public ArrayList<Epic> getAll() {
        return epicRepo.getAll();
    }

    @Override
    public Epic update(EpicUpdationData data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }

        Epic savedEpic = epicRepo.get(data.getId());

        if (savedEpic == null) {
            throw new NoSuchElementException("epic not found");
        }

        savedEpic.setName(data.getName());
        savedEpic.setDescription(data.getDescription());

        return epicRepo.update(savedEpic);
    }

    @Override
    public void remove(Integer id) {
        Epic epic = epicRepo.get(id);

        if (epic == null) {
            return;
        }

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

        if (epic == null) {
            throw new NoSuchElementException("epic not found");
        }

        return epic.getSubtasks();
    }
}
