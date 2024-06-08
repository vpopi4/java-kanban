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
        return "Task {"
                + "\n\tid          = " + id
                + "\n\tname        = " + name
                + "\n\tdescription = " + description
                + "\n\tstatus      = " + status
                + "\n\tduration    = " + duration
                + "\n\tstartTime   = " + startTime
                + "\n\tepicId      = " + epicId
                + "\n}";
    }
}
