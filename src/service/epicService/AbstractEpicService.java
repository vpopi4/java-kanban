package service.epicService;

import java.util.ArrayList;

import dtos.EpicUpdationData;
import dtos.TaskCreationData;
import interfaces.EpicService;
import interfaces.repository.EpicRepository;
import interfaces.repository.SubtaskRepository;
import model.Epic;
import model.Subtask;
import model.Task;
import service.HistoryService;
import util.IdGenerator;

public abstract class AbstractEpicService implements EpicService {
    private final EpicRepository epicRepo;
    private final SubtaskRepository subtaskRepo;
    private final IdGenerator idGenerator;
    private final HistoryService<Task> historyService;

    public AbstractEpicService(
            EpicRepository epicRepository,
            SubtaskRepository subtaskRepository,
            IdGenerator idGenerator,
            HistoryService<Task> historyService) {
        this.epicRepo = epicRepository;
        this.subtaskRepo = subtaskRepository;
        this.idGenerator = idGenerator;
        this.historyService = historyService;
    }

    @Override
    public Epic create(TaskCreationData data) {
        Epic epic = new Epic(idGenerator.generateNewId(), data);

        return epicRepo.create(epic);
    }

    @Override
    public Epic get(Integer id) {
        Epic epic = epicRepo.get(id);
        historyService.add(epic);
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
