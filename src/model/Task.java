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
        if (name == null) {
            throw new IllegalArgumentException("name should not be null");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException("name should not be empty");
        }

        if (name.contains("\n")) {
            throw new IllegalArgumentException("name must be one line string");
        }

        this.name = name.trim();
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("description should not be null");
        }

        if (description.isBlank()) {
            throw new IllegalArgumentException("description should not be empty");
        }

        if (description.contains("\n")) {
            throw new IllegalArgumentException("description must be one line string");
        }

        this.description = description.trim();
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
