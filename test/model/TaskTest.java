package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void shouldReturnExpectedStringRepresentation() {
        TaskCreationData data = new TaskCreationData("Test Task", "Description");
        Task task = new Task(1, data);

        String expectedString = "Task{" +
                "\n\tid=1" +
                ",\n\tname='Test Task'" +
                ",\n\tdescription='Description'" +
                ",\n\tstatus=NEW" +
                "\n}";
        String actualString = task.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void shouldBeEqualIfAllPropertiesAreEqual() {
        TaskCreationData data1 = new TaskCreationData("Test Task", "Description");
        TaskCreationData data2 = new TaskCreationData("Test Task", "Description");
        Task task1 = new Task(1, data1);
        Task task2 = new Task(1, data2);

        assertEquals(task1, task2);
    }

    @Test
    public void shouldNotBeEqualIfOnlyIdsAreEqual() {
        TaskCreationData data1 = new TaskCreationData("Test Task", "Description");
        TaskCreationData data2 = new TaskCreationData("Test Task", "Different Description");
        Task task1 = new Task(1, data1);
        Task task2 = new Task(1, data2);

        assertNotEquals(task1, task2);
    }
}
