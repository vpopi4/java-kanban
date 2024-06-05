package util;

import interfaces.model.Taskable;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.Arrays;
import java.util.List;

public class TaskableFactory {
    public static String serialize(Taskable entity) {
        // id,type,name,status,description,epic
        String record = entity.getId() + "," +
                entity.getType() + "," +
                entity.getName() + "," +
                entity.getStatus() + "," +
                entity.getDescription() + ",";

        if (entity.getType() == TaskType.SUBTASK) {
            record += ((Subtask) entity).getEpicId();
        }

        return record;
    }

    public static Taskable deserialize(String string) {
        List<String> record = Arrays.stream(string.split(",", -1)).toList();

        Integer id = Integer.parseInt(record.get(0));
        TaskType type = TaskType.valueOf(record.get(1));
        String name = record.get(2);
        TaskStatus status = TaskStatus.valueOf(record.get(3));
        String description = record.get(4);

        if (type == TaskType.TASK) {
            Task task = new Task(id);
            task.setName(name);
            task.setStatus(status);
            task.setDescription(description);
            return task;
        } else if (type == TaskType.EPIC) {
            Epic epic = new Epic(id);
            epic.setName(name);
            epic.setStatus(status);
            epic.setDescription(description);
            return epic;
        } else if (type == TaskType.SUBTASK) {
            Integer epicId = Integer.parseInt(record.get(5));

            Subtask task = new Subtask(id, epicId);
            task.setName(name);
            task.setStatus(status);
            task.setDescription(description);
            return task;
        }
        throw new IllegalArgumentException("record can not be resolved");
    }
}
