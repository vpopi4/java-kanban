package service;

import java.util.ArrayList;

import interfaces.EpicService;
import interfaces.HistoryManager;
import interfaces.SubtaskService;
import interfaces.TaskManager;
import interfaces.TaskService;
import interfaces.repository.EpicRepository;
import interfaces.repository.SubtaskRepository;
import interfaces.repository.TaskRepository;
import model.Task;
import repository.InMemoryEpicRepository;
import repository.InMemorySubtaskRepository;
import repository.InMemoryTaskRepository;
import service.epicService.InMemoryEpicService;
import service.subtaskService.InMemorySubtaskService;
import service.taskService.InMemoryTaskService;
import util.InMemoryHistoryManager;
import util.IdGenerator;

public class InMemoryTaskManager implements TaskManager {
    private final TaskRepository taskRepository;
    private final EpicRepository epicRepository;
    private final SubtaskRepository subtaskRepository;

    private final TaskService taskService;
    private final EpicService epicService;
    private final SubtaskService subtaskService;

    private final HistoryManager<Task> historyService;

    public InMemoryTaskManager() {
        IdGenerator idGenerator = new IdGenerator();
        historyService = new InMemoryHistoryManager();

        taskRepository = new InMemoryTaskRepository();
        epicRepository = new InMemoryEpicRepository();
        subtaskRepository = new InMemorySubtaskRepository();

        taskService = new InMemoryTaskService(
                taskRepository,
                idGenerator,
                historyService
        );
        epicService = new InMemoryEpicService(
                epicRepository,
                subtaskRepository,
                idGenerator,
                historyService
        );
        subtaskService = new InMemorySubtaskService(
                epicRepository,
                subtaskRepository,
                idGenerator,
                historyService
        );
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
    public ArrayList<Task> getHistory() {
        return historyService.getHistory();
    }
}
