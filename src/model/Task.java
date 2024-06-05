package model;

import interfaces.model.Taskable;
import util.TaskStatus;
import util.TaskType;

import java.util.Objects;

public class Task implements Taskable {
    protected Integer id;
    protected String name;
    protected String description;
    protected TaskStatus status;

    public Task(Integer id) {
        this.id = id;
        this.status = TaskStatus.NEW;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public TaskType getType() {
        return TaskType.TASK;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
                "(%d) %s: %s - %s [%s]",
                id,
                getType(),
                name,
                description,
                status
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }
}
