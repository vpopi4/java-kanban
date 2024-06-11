package repository;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TaskStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedRepositoryTest {
    private FileBackedRepository repository;
    private Path pathToBackup;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    public void setUp() throws IOException {
        pathToBackup = Files.createTempFile("test", ".csv");
        repository = new FileBackedRepository(pathToBackup);

        task = new Task(1);
        task.setName("Task 1");
        task.setDescription("Description 1");
        task.setStatus(TaskStatus.NEW);

        epic = new Epic(2);
        epic.setName("Epic 1");
        epic.setDescription("Description 2");
        task.setStatus(TaskStatus.NEW);

        subtask = new Subtask(3, 2);
        subtask.setName("Subtask 1");
        subtask.setDescription("Description 3");
        task.setStatus(TaskStatus.NEW);
    }

    @Test
    public void testCreateAndSaveTask() {
        repository.create(task);
        repository.save();
        assertTrue(Files.exists(pathToBackup));
    }

    @Test
    public void testCreateAndSaveEpic() {
        repository.create(epic);
        repository.save();
        assertTrue(Files.exists(pathToBackup));
    }

    @Test
    public void testCreateAndSaveSubtask() {
        repository.create(subtask);
        repository.save();
        assertTrue(Files.exists(pathToBackup));
    }

    @Test
    public void testClearAndLoadBackup() {
        repository.create(task);
        repository.create(epic);
        repository.create(subtask);
        repository.save();

        repository.clearAndLoadBackup();

        Optional<Task> loadedTask = repository.getTaskById(1);
        Optional<Epic> loadedEpic = repository.getEpicById(2);
        Optional<Subtask> loadedSubtask = repository.getSubtaskById(3);

        assertTrue(loadedTask.isPresent());
        assertTrue(loadedEpic.isPresent());
        assertTrue(loadedSubtask.isPresent());

        assertEquals("Task 1", loadedTask.get().getName());
        assertEquals("Epic 1", loadedEpic.get().getName());
        assertEquals("Subtask 1", loadedSubtask.get().getName());
    }

    @Test
    public void testRestoreRelations() {
        repository.create(epic);
        repository.create(subtask);
        repository.save();

        repository.clearAndLoadBackup();

        Epic loadedEpic = repository.getEpicById(2).orElseThrow();
        Subtask loadedSubtask = repository.getSubtaskById(3).orElseThrow();

        assertTrue(loadedEpic.getSubtaskIds().contains(loadedSubtask.getId()));
    }

    @Test
    public void testSaveAndClear() {
        repository.create(task);
        repository.save();
        repository.clear();

        assertTrue(repository.getAllTasks().isEmpty());
    }

    @Test
    public void testGetNonExistentTaskAfterLoad() {
        repository.clearAndLoadBackup();
        assertFalse(repository.getTaskById(99).isPresent());
    }

    @Test
    public void testUpdateTaskAndSave() {
        repository.create(task);
        repository.save();

        Task updatedTask = new Task(1);
        updatedTask.setName("Updated Task");
        updatedTask.setDescription("Updated Description");

        repository.update(updatedTask);
        repository.save();

        repository.clearAndLoadBackup();

        Task loadedTask = repository.getTaskById(1).orElseThrow();
        assertEquals("Updated Task", loadedTask.getName());
        assertEquals("Updated Description", loadedTask.getDescription());
    }

    @Test
    public void testRemoveTaskAndSave() {
        repository.create(task);
        repository.save();

        repository.remove(1);
        repository.save();

        repository.clearAndLoadBackup();

        assertFalse(repository.getTaskById(1).isPresent());
    }

    @Test
    public void testRemoveAllTasksAndSave() {
        repository.create(task);
        repository.save();

        repository.removeAllTasks();
        repository.save();

        repository.clearAndLoadBackup();

        assertTrue(repository.getAllTasks().isEmpty());
    }

    @Test
    public void testRemoveAllEpicsAndSubtasksAndSave() {
        repository.create(epic);
        repository.create(subtask);
        repository.save();

        repository.removeAllEpicsAndSubtasks();
        repository.save();

        repository.clearAndLoadBackup();

        assertTrue(repository.getAllEpics().isEmpty());
        assertTrue(repository.getAllSubtasks().isEmpty());
    }

    @Test
    public void testRemoveAllSubtasksAndSave() {
        repository.create(subtask);
        repository.save();

        repository.removeAllSubtasks();
        repository.save();

        repository.clearAndLoadBackup();

        assertTrue(repository.getAllSubtasks().isEmpty());
    }

    @Test
    public void testUpdateNonExistentTask() {
        Task nonExistentTask = new Task(99);
        nonExistentTask.setName("Non-existent Task");
        nonExistentTask.setDescription("Description");

        assertThrows(NoSuchElementException.class, () -> repository.update(nonExistentTask));
    }

    @Test
    public void testRemoveNonExistentTask() {
        assertDoesNotThrow(() -> repository.remove(99));
    }
}
