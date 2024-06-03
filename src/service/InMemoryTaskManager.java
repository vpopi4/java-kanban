package service;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import interfaces.service.EpicService;
import interfaces.service.SubtaskService;
import interfaces.service.TaskService;
import model.Task;
import repository.InMemoryRepository;
import service.epicService.InMemoryEpicService;
import service.subtaskService.InMemorySubtaskService;
import service.taskService.InMemoryTaskService;
import util.IdGenerator;
import util.InMemoryHistoryManager;

import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager;
    protected TaskService taskService;
    protected EpicService epicService;
    protected SubtaskService subtaskService;

    public InMemoryTaskManager() {
        InMemoryRepository repository = new InMemoryRepository();
        IdGenerator idGenerator = new IdGenerator();

        historyManager = new InMemoryHistoryManager();
        taskService = new InMemoryTaskService(repository, idGenerator, historyManager);
        epicService = new InMemoryEpicService(repository, idGenerator, historyManager);
        subtaskService = new InMemorySubtaskService(repository, idGenerator, historyManager);
    }

    @Override
    public TaskService getTaskService() {
        return taskService;
    }

    @Override
    public EpicService getEpicService() {
        return epicService;
    }

    @Override
    public SubtaskService getSubtaskService() {
        return subtaskService;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
