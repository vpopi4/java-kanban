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
    public static final Path DEFAULT_PATH_TO_BACKUP = Path.of("store.csv");
    private FileBackedRepository repository;

    public FileBackedTaskManager() {
        this(DEFAULT_PATH_TO_BACKUP);
    }

    public FileBackedTaskManager(Path pathToBackup) {
        super();
        repository = new FileBackedRepository(pathToBackup);
        IdGenerator idGenerator = new IdGenerator();
        HistoryManager historyService = new InMemoryHistoryManager();

        repository.clearAndLoadBackup();

        taskService = new FileBackedTaskService(repository, idGenerator, historyService);
        epicService = new FileBackedEpicService(repository, idGenerator, historyService);
        subtaskService = new FileBackedSubtaskService(repository, idGenerator, historyService);
    }

    public void clearAndLoadBackup() {
        repository.clearAndLoadBackup();
    }
}