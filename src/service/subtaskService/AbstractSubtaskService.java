package service.subtaskService;

import java.util.ArrayList;

import dtos.TaskCreationData;
import enums.TaskStatus;
import interfaces.SubtaskService;
import interfaces.repository.EpicRepository;
import interfaces.repository.SubtaskRepository;
import model.Epic;
import model.Subtask;
import model.Task;
import util.History;
import util.IdGenerator;

public class AbstractSubtaskService implements SubtaskService {
    private final EpicRepository epicRepo;
    private final SubtaskRepository subtaskRepo;
    private final IdGenerator idGenerator;
    private final History<Task> historyService;

    public AbstractSubtaskService(
            EpicRepository epicRepository,
            SubtaskRepository subtaskRepository,
            IdGenerator idGenerator,
            History<Task> historyService
    ) {
        this.epicRepo = epicRepository;
        this.subtaskRepo = subtaskRepository;
        this.idGenerator = idGenerator;
        this.historyService = historyService;
    }

    @Override
    public Subtask create(TaskCreationData data, Integer epicId) {
        Epic epic = epicRepo.get(epicId);
        Subtask subtask = new Subtask(idGenerator.generateNewId(), data, epic);
        
        epic.addSubtask(subtask);
           
        epicRepo.update(epic);
        return subtaskRepo.create(subtask);
    }

    @Override
    public Subtask get(Integer id) {
        Subtask subtask = subtaskRepo.get(id);
        historyService.add(subtask);
        return subtask;
    }

    @Override
    public ArrayList<Subtask> getAll() {
        return subtaskRepo.getAll();
    }

    @Override
    public Subtask update(Subtask subtask) {
        Subtask savesSubtask = subtaskRepo.get(subtask.getId());

        savesSubtask.setName(subtask.getName());
        savesSubtask.setDescription(subtask.getDescription());
        savesSubtask.setStatus(subtask.getStatus());

        Epic epic = savesSubtask.getEpic();

        epic.setStatus(calculateStatusOfEpic(epic));

        epicRepo.update(epic);
        return subtaskRepo.update(savesSubtask);
    }

    @Override
    public void remove(Integer id) {
        Subtask subtask = subtaskRepo.get(id);
        Epic epic = subtask.getEpic();

        epic.getSubtasks().remove(subtask);
        epic.setStatus(calculateStatusOfEpic(epic));
        
        subtaskRepo.remove(id);
        epicRepo.update(epic);
    }

    @Override
    public void removeAll() {
        subtaskRepo.removeAll();

        for (Epic epic : epicRepo.getAll()) {
            epic.getSubtasks().clear();
            epic.setStatus(TaskStatus.NEW);
        }
    }

    private TaskStatus calculateStatusOfEpic(Epic epic) {
        int size = epic.getSubtasks().size();

        if (size == 0) {
            return TaskStatus.NEW;
        }

        int countOfNew = 0;
        int countOfDone = 0;

        for (Subtask subtask : epic.getSubtasks()) {
            if (subtask.getStatus() == TaskStatus.NEW) {
                countOfNew++;
            } else if (subtask.getStatus() == TaskStatus.DONE) {
                countOfDone++;
            }
        }

        if (countOfNew == size) {
            return TaskStatus.NEW;
        } else if (countOfDone == size) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }
}
