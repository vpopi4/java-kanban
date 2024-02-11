package service;

import interfaces.EpicService;
import interfaces.SubtaskService;
import interfaces.TaskService;
import interfaces.repository.EpicRepository;
import interfaces.repository.SubtaskRepository;
import interfaces.repository.TaskRepository;
import repository.InMemoryEpicRepository;
import repository.InMemorySubtaskRepository;
import repository.InMemoryTaskRepository;
import service.epicService.InMemoryEpicService;
import service.subtaskService.InMemorySubtaskService;
import service.taskService.InMemoryTaskService;

public class InMemoryTaskManager {
    private final TaskRepository taskRepository;
    private final EpicRepository epicRepository;
    private final SubtaskRepository subtaskRepository;

    private final TaskService taskService;
    private final EpicService epicService;
    private final SubtaskService subtaskService;

    public InMemoryTaskManager() {
        IdGenerator idGenerator = new IdGenerator();

        taskRepository = new InMemoryTaskRepository();
        epicRepository = new InMemoryEpicRepository();
        subtaskRepository = new InMemorySubtaskRepository();

        taskService = new InMemoryTaskService(taskRepository, idGenerator);
        epicService = new InMemoryEpicService(epicRepository, subtaskRepository, idGenerator);
        subtaskService = new InMemorySubtaskService(epicRepository, subtaskRepository, idGenerator);
        
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
}
