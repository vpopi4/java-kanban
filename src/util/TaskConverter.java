package util;

import com.google.gson.*;
import interfaces.model.Taskable;
import model.Epic;
import model.Subtask;
import model.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TaskConverter {
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public static String toCsvRecord(Taskable entity) {
        // id,type,name,status,description,duration,startTime,epic
        Duration duration = entity.getDuration() == null
                ? Duration.ZERO
                : entity.getDuration();

        String record = entity.getId() + "," +
                entity.getType() + "," +
                entity.getName() + "," +
                entity.getStatus() + "," +
                entity.getDescription() + "," +
                duration.toMillis() + "," +
                entity.getStartTime() + ",";

        if (entity.getType() == TaskType.SUBTASK) {
            record += ((Subtask) entity).getEpicId();
        }

        return record;
    }

    public static Taskable fromCsvRecord(String string) {
        List<String> record = Arrays.stream(string.split(",", -1)).toList();

        Integer id = Integer.parseInt(record.get(0));
        TaskType type = TaskType.valueOf(record.get(1));
        String name = record.get(2);
        TaskStatus status = TaskStatus.valueOf(record.get(3));
        String description = record.get(4);
        Duration duration = Duration.ofMillis(Long.parseLong(record.get(5)));
        LocalDateTime startTime = Objects.equals(record.get(6), "null")
                ? null
                : LocalDateTime.parse(record.get(6));

        if (type == TaskType.TASK) {
            Task task = new Task(id);
            task.setName(name);
            task.setStatus(status);
            task.setDescription(description);
            task.setDuration(duration);
            task.setStartTime(startTime);
            return task;
        } else if (type == TaskType.EPIC) {
            Epic epic = new Epic(id);
            epic.setName(name);
            epic.setStatus(status);
            epic.setDescription(description);
            epic.setDuration(duration);
            epic.setStartTime(startTime);
            return epic;
        } else if (type == TaskType.SUBTASK) {
            Integer epicId = Integer.parseInt(record.get(7));

            Subtask task = new Subtask(id, epicId);
            task.setName(name);
            task.setStatus(status);
            task.setDescription(description);
            task.setDuration(duration);
            task.setStartTime(startTime);
            return task;
        }
        throw new IllegalArgumentException("record can not be resolved");
    }

    public static String toJson(Taskable taskable) {
        return gson.toJson(taskable);
    }

    public static String toJson(List<? extends Taskable> tasks) {
        return gson.toJson(tasks);
    }

    public static Taskable formJson(String json) {
        Taskable taskable = gson.fromJson(json, Task.class);
        if (taskable.getType() == TaskType.EPIC) {
            taskable = gson.fromJson(json, Epic.class);
        } else if (taskable.getType() == TaskType.SUBTASK) {
            taskable = gson.fromJson(json, Subtask.class);
        }
        return taskable;
    }

    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public JsonElement serialize(LocalDateTime src,
                                     Type typeOfSrc,
                                     JsonSerializationContext context) {
            return new JsonPrimitive(src.format(formatter));
        }

        @Override
        public LocalDateTime deserialize(
                JsonElement json,
                Type typeOfT,
                JsonDeserializationContext context
        ) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), formatter);
        }
    }

    private static class DurationAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {
        @Override
        public JsonElement serialize(Duration src,
                                     Type typeOfSrc,
                                     JsonSerializationContext context) {
            return new JsonPrimitive(src.toMillis());
        }

        @Override
        public Duration deserialize(
                JsonElement json,
                Type typeOfT,
                JsonDeserializationContext context
        ) throws JsonParseException {
            return Duration.ofMillis(json.getAsLong());
        }
    }
}
