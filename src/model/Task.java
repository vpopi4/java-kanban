package model;

import interfaces.model.Taskable;
import util.TaskStatus;
import util.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Taskable {
    protected Integer id;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(Integer id) {
        this.id = id;
        this.status = TaskStatus.NEW;
        this.duration = Duration.ZERO;
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
        this.name = validateNameAndDescription("name", name);
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = validateNameAndDescription("description", description);
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    private String validateNameAndDescription(String key, String value) {
        if (value == null) {
            throw new IllegalArgumentException(key + " should not be null");
        }

        if (value.isBlank()) {
            throw new IllegalArgumentException(key + " should not be empty");
        }

        if (value.contains("\n")) {
            throw new IllegalArgumentException(key + " must be one line string");
        }

        return value.trim();
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
                + "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id)
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && status == task.status
                && Objects.equals(duration, task.duration)
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, duration, startTime);
    }
}
