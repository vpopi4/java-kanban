package repository;

import interfaces.model.Taskable;
import model.Epic;
import model.Subtask;
import util.ManagerSaveException;
import util.TaskConverter;
import util.TaskType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedRepository extends InMemoryRepository {
    private final Path pathToBackup;

    public FileBackedRepository(Path pathToBackup) {
        super();
        this.pathToBackup = pathToBackup;
    }

    public void save() {
        try {
            String header = "id,type,name,status,description,duration,startTime,epic\n";
            StringBuilder sb = new StringBuilder(header);

            for (Map.Entry<Integer, Taskable> entry : store.entrySet()) {
                sb.append(TaskConverter.toCsvRecord(entry.getValue()));
                sb.append("\n");
            }

            Files.writeString(
                    pathToBackup,
                    sb.toString(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    public void clear() {
        store.clear();
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public void clearAndLoadBackup() {
        try {
            clear();

            if (!Files.exists(pathToBackup)) {
                return;
            }

            String[] data = Files.readString(
                    pathToBackup,
                    StandardCharsets.UTF_8
            ).split("\n");

            for (int i = 1; i < data.length; i++) {
                Taskable record = TaskConverter.fromCsvRecord(data[i]);
                backToTheStore(record);
            }

            restoreRelations();
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    private void backToTheStore(Taskable record) {
        store.put(record.getId(), record);

        if (record.getType() == TaskType.TASK) {
            tasks.add(record.getId());
        } else if (record.getType() == TaskType.EPIC) {
            epics.add(record.getId());
        } else if (record.getType() == TaskType.SUBTASK) {
            subtasks.add(record.getId());
        }
    }

    private void restoreRelations() {
        List<Epic> allEpics = getAllEpics();
        HashMap<Integer, List<Integer>> subtaskIdsOfEpics = new HashMap<>();

        for (Epic epic : allEpics) {
            subtaskIdsOfEpics.put(epic.getId(), new ArrayList<>());
        }

        for (Subtask subtask : getAllSubtasks()) {
            subtaskIdsOfEpics
                    .get(subtask.getEpicId())
                    .add(subtask.getId());
        }

        for (Epic epic : allEpics) {
            epic.setSubtaskIds(subtaskIdsOfEpics.get(epic.getId()));
        }
    }
}
