package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TaskStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    private FileBackedTaskManager manager;
    private Path tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("test", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @Test
    public void testSaveAndLoadEmptyFile() {
        manager.clearAndLoadBackup();

        assertTrue(manager.getTaskService().getAll().isEmpty(), "Tasks should be empty");
        assertTrue(manager.getEpicService().getAll().isEmpty(), "Epics should be empty");
        assertTrue(manager.getSubtaskService().getAll().isEmpty(), "Subtasks should be empty");
    }

    @Test
    public void testSaveAndLoadMultipleTasks() {
        // Arrange
        Task task1 = manager.getTaskService()
                .create("Task 1", "Description 1");

        Task task2 = manager.getTaskService()
                .create("Task 2", "Description 2");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        manager.getTaskService().update(task2);

        Epic epic1 = manager.getEpicService()
                .create("Epic 1", "Description 3");

        Subtask subtask1 = manager.getSubtaskService()
                .create(epic1.getId(), "Subtask 1", "Description 4");
        subtask1.setStatus(TaskStatus.DONE);
        manager.getSubtaskService().update(subtask1);

        // Act
        manager.clearAndLoadBackup();

        List<Task> tasks = manager.getTaskService().getAll();
        List<Epic> epics = manager.getEpicService().getAll();
        List<Subtask> subtasks = manager.getSubtaskService().getAll();

        assertEquals(2, tasks.size(), "There should be 2 tasks");
        assertEquals(1, epics.size(), "There should be 1 epic");
        assertEquals(1, subtasks.size(), "There should be 1 subtask");

        assertEquals(task1, tasks.get(0));
        assertEquals(task2, tasks.get(1));
        assertEquals(epic1, epics.get(0));
        assertEquals(subtask1, subtasks.get(0));
    }

    @Test
    public void testLoadFromFile() throws IOException {
        String csvData = "id,type,name,status,description,duration,startTime,epic\n" +
                "1,TASK,Task 1,NEW,Description 1,0,null,\n" +
                "2,TASK,Task 2,IN_PROGRESS,Description 2,0,null,\n" +
                "3,EPIC,Epic 1,DONE,Description 3,0,null,\n" +
                "4,SUBTASK,Subtask 1,DONE,Description 4,0,null,3\n";

        Files.writeString(tempFile, csvData);

        manager.clearAndLoadBackup();

        List<Task> tasks = manager.getTaskService().getAll();
        List<Epic> epics = manager.getEpicService().getAll();
        List<Subtask> subtasks = manager.getSubtaskService().getAll();

        assertEquals(2, tasks.size(), "There should be 2 tasks");
        assertEquals(1, epics.size(), "There should be 1 epic");
        assertEquals(1, subtasks.size(), "There should be 1 subtask");

        Task task1 = new Task(1);
        task1.setName("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);
        task1.setDuration(Duration.ZERO);
        Task task2 = new Task(2);
        task2.setName("Task 2");
        task2.setDescription("Description 2");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setDuration(Duration.ZERO);
        Epic epic1 = new Epic(3);
        epic1.setName("Epic 1");
        epic1.setDescription("Description 3");
        epic1.setStatus(TaskStatus.DONE);
        epic1.setDuration(Duration.ZERO);
        Subtask subtask1 = new Subtask(4, 3);
        subtask1.setName("Subtask 1");
        subtask1.setDescription("Description 4");
        subtask1.setStatus(TaskStatus.DONE);
        subtask1.setDuration(Duration.ZERO);

        assertEquals(task1, tasks.get(0));
        assertEquals(task2, tasks.get(1));
        assertEquals(epic1, epics.get(0));
        assertEquals(subtask1, subtasks.get(0));
    }
}
