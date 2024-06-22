package util;

import interfaces.model.Taskable;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class TaskConverterTest {
    @Test
    public void testSerializeMethod_forTask() {
        // Arrange
        Task entity = new Task(0);
        entity.setName("Some name");
        entity.setDescription("Lorem ipsum");
        entity.setStatus(TaskStatus.NEW);
        entity.setDuration(Duration.ofMillis(60_000));

        // Act
        String expected = "0,TASK,Some name,NEW,Lorem ipsum,60000,null,";
        String actual = TaskConverter.toCsvRecord(entity);

        // Assert
        assertEquals(expected, actual);

        // Arrange
        entity.setStatus(TaskStatus.DONE);
        entity.setDescription("  leading and trailing space here!   ");
        entity.setDuration(Duration.ZERO);
        entity.setStartTime(LocalDateTime.of(2024, Month.JUNE, 10, 10, 0));

        // Act
        expected = "0,TASK,Some name,DONE,leading and trailing space here!,0,2024-06-10T10:00,";
        actual = TaskConverter.toCsvRecord(entity);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeMethod_forEpic() {
        Epic entity = new Epic(0);
        entity.setName("Some name");
        entity.setDescription("Lorem ipsum");
        entity.setStatus(TaskStatus.NEW);
        entity.setDuration(Duration.ofMillis(40000));

        String expected = "0,EPIC,Some name,NEW,Lorem ipsum,40000,null,";

        String actual = TaskConverter.toCsvRecord(entity);

        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeMethod_forSubtask() {
        Subtask entity = new Subtask(1, 0);
        entity.setName("Some name");
        entity.setDescription("Lorem ipsum");
        entity.setStatus(TaskStatus.DONE);
        entity.setDuration(Duration.ofMillis(666));
        entity.setStartTime(LocalDateTime.of(2024, Month.JUNE, 10, 10, 0));

        String expected = "1,SUBTASK,Some name,DONE,Lorem ipsum,666,2024-06-10T10:00,0";

        String actual = TaskConverter.toCsvRecord(entity);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialization() {
        String record = "0,EPIC,Some name,NEW,Lorem ipsum,90000,null";

        Taskable deserializedRecord = TaskConverter.fromCsvRecord(record);

        assertEquals(deserializedRecord.getType(), TaskType.EPIC);
        assertInstanceOf(Epic.class, deserializedRecord);
        assertEquals(0, deserializedRecord.getId());
        assertEquals("Some name", deserializedRecord.getName());
        assertEquals("Lorem ipsum", deserializedRecord.getDescription());
        assertEquals(TaskStatus.NEW, deserializedRecord.getStatus());
        assertEquals(Duration.ofMillis(90_000), deserializedRecord.getDuration());
        assertNull(deserializedRecord.getStartTime());

        record = "1,SUBTASK,SubSome name,IN_PROGRESS,Lorem ipsum dollar,0,null,0";
        deserializedRecord = TaskConverter.fromCsvRecord(record);

        assertEquals(deserializedRecord.getType(), TaskType.SUBTASK);
        assertEquals(1, deserializedRecord.getId());
        assertInstanceOf(Subtask.class, deserializedRecord);
        assertEquals("SubSome name", deserializedRecord.getName());
        assertEquals("Lorem ipsum dollar", deserializedRecord.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, deserializedRecord.getStatus());
        assertEquals(0, ((Subtask) deserializedRecord).getEpicId());
    }
}