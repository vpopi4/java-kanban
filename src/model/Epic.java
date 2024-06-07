package model;

import util.TaskType;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds;

    public Epic(Integer id) {
        super(id);
        subtaskIds = new ArrayList<>();
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    public void addSubtaskId(Integer id) {
        subtaskIds.add(id);
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds.clear();
        this.subtaskIds.addAll(subtaskIds);
    }

    @Override
    public String toString() {
        return String.format(
                "(%d) %s: %s - %s [%s] Subtasks:%s",
                id,
                getType(),
                name,
                description,
                status,
                getSubtaskIds()
        );
    }
}
