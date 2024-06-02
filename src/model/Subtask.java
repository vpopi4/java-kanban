package model;

import util.TaskStatus;
import util.TaskType;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(Integer id, Epic epic) {
        super(id);
        this.epicId = epic.getId();
    }

    public Subtask(Integer id, Integer epicId) {
        super(id);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        // id,type,name,status,description,epic
        return id + "," +
                TaskType.TASK + "," +
                name + "," +
                status + "," +
                description + "," +
                epicId + "\n";
    }

    public static Subtask fromString(String string) {
        String[] record = string.split(",");

        Integer id = Integer.parseInt(record[0]);
        TaskType type = TaskType.valueOf(record[1]);
        String name = record[2];
        TaskStatus status = TaskStatus.valueOf(record[3]);
        String description = record[4];
        Integer epicId = Integer.parseInt(record[5]);

        if (type == TaskType.SUBTASK) {
            Subtask task = new Subtask(id, epicId);
            task.setName(name);
            task.setStatus(status);
            task.setDescription(description);
            return task;
        }

        throw new IllegalArgumentException("record can not be resolved as subtask");
    }
}
