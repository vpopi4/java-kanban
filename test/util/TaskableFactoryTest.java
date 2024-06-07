package util;

import interfaces.model.Taskable;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class TaskableFactoryTest {
    @Test
    public void testSerializeMethod_forTask() {
        // Arrange
        Task entity = new Task(0);
        entity.setName("Some name");
        entity.setDescription("Lorem ipsum");
        entity.setStatus(TaskStatus.NEW);

        // Act
        String expected = "0,TASK,Some name,NEW,Lorem ipsum,";
        String actual = TaskableFactory.serialize(entity);

        // Assert
        assertEquals(expected, actual);

        // Arrange
        entity.setStatus(TaskStatus.DONE);
        entity.setDescription("  leading and trailing space here!   ");

        // Act
        expected = "0,TASK,Some name,DONE,leading and trailing space here!,";
        actual = TaskableFactory.serialize(entity);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeMethod_forEpic() {
        Epic entity = new Epic(0);
        entity.setName("Some name");
        entity.setDescription("Lorem ipsum");
        entity.setStatus(TaskStatus.NEW);

        String expected = "0,EPIC,Some name,NEW,Lorem ipsum,";

        String actual = TaskableFactory.serialize(entity);

        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeMethod_forSubtask() {
        Subtask entity = new Subtask(1, 0);
        entity.setName("Some name");
        entity.setDescription("Lorem ipsum");
        entity.setStatus(TaskStatus.DONE);

        String expected = "1,SUBTASK,Some name,DONE,Lorem ipsum,0";

        String actual = TaskableFactory.serialize(entity);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserialization() {
        String record = "0,EPIC,Some name,NEW,Lorem ipsum,";

        Taskable deserializedRecord = TaskableFactory.deserialize(record);

        assertEquals(deserializedRecord.getType(), TaskType.EPIC);
        assertInstanceOf(Epic.class, deserializedRecord);
        assertEquals(0, deserializedRecord.getId());
        assertEquals("Some name", deserializedRecord.getName());
        assertEquals("Lorem ipsum", deserializedRecord.getDescription());
        assertEquals(TaskStatus.NEW, deserializedRecord.getStatus());

        record = "1,SUBTASK,SubSome name,IN_PROGRESS,Lorem ipsum dollar,0";
        deserializedRecord = TaskableFactory.deserialize(record);

        assertEquals(deserializedRecord.getType(), TaskType.SUBTASK);
        assertEquals(1, deserializedRecord.getId());
        assertInstanceOf(Subtask.class, deserializedRecord);
        assertEquals("SubSome name", deserializedRecord.getName());
        assertEquals("Lorem ipsum dollar", deserializedRecord.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, deserializedRecord.getStatus());
        assertEquals(0, ((Subtask) deserializedRecord).getEpicId());
    }
}