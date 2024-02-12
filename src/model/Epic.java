package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Subtask> subtasks;

    public Epic(int id, TaskCreationData dto) {
        super(id, dto);
        subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "\n\tid=" + id +
                ",\n\tname='" + name + '\'' +
                ",\n\tdescription='" + description + '\'' +
                ",\n\tstatus=" + status +
                ",\n\tsubtasksIDs=" + getSubtaskIDs() +
                "\n}";
    }

    private ArrayList<Integer> getSubtaskIDs() {
        var list = new ArrayList<Integer>();

        for (Subtask subtask: subtasks) {
            list.add(subtask.getId());
        }

        return list;
    }
}
