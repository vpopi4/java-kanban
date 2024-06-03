package service;

import interfaces.HistoryManager;
import repository.FileBackedRepository;
import service.epicService.FileBackedEpicService;
import service.subtaskService.FileBackedSubtaskService;
import service.taskService.FileBackedTaskService;
import util.IdGenerator;
import util.InMemoryHistoryManager;

import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public FileBackedTaskManager() {
        super();
        FileBackedRepository repository = new FileBackedRepository(Path.of("store.csv"));
        IdGenerator idGenerator = new IdGenerator();
        HistoryManager historyService = new InMemoryHistoryManager();


        taskService = new FileBackedTaskService(repository, idGenerator, historyService);
        epicService = new FileBackedEpicService(repository, idGenerator, historyService);
        subtaskService = new FileBackedSubtaskService(repository, idGenerator, historyService);
    }
}