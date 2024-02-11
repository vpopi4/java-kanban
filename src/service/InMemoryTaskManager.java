package service;

import java.util.ArrayList;

import interfaces.EpicService;
import interfaces.SubtaskService;
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
import util.History;
import util.IdGenerator;

public class InMemoryTaskManager {
    private final TaskRepository taskRepository;
    private final EpicRepository epicRepository;
    private final SubtaskRepository subtaskRepository;

    private final TaskService taskService;
    private final EpicService epicService;
    private final SubtaskService subtaskService;

    private final History<Task> historyService;

    public InMemoryTaskManager() {
        IdGenerator idGenerator = new IdGenerator();
        historyService = new History<>();

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

    public TaskService getTaskService() {
        return taskService;
    }

    public EpicService getEpicService() {
        return epicService;
    }

    public SubtaskService getSubtaskService() {
        return subtaskService;
    }

    public ArrayList<Task> getHistory() {
        return historyService.getHistory();
    }
}
