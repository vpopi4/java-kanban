package model;

import util.TaskType;

public class Subtask extends Task {
    private final Integer epicId;

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
        return String.format(
                "(%d) %s: %s - %s [%s] Epic:%d",
                id,
                getType(),
                name,
                description,
                status,
                getEpicId()
        );
    }
}
