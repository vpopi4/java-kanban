package service;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import interfaces.repository.Repository;
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
import util.TaskManagerConfig;

import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final TaskService taskService;
    private final EpicService epicService;
    private final SubtaskService subtaskService;
    private final TaskManagerConfig config;

    public InMemoryTaskManager() {
        Repository repository = new InMemoryRepository();
        IdGenerator idGenerator = new IdGenerator();
        HistoryManager historyService = new InMemoryHistoryManager();

        TaskManagerConfig config = new TaskManagerConfig(
                repository,
                idGenerator,
                historyService
        );

        this.config = config;
        taskService = new InMemoryTaskService(config);
        epicService = new InMemoryEpicService(config);
        subtaskService = new InMemorySubtaskService(config);
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
        return config.historyManager().getHistory();
    }
}
