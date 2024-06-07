package service.epicService;

import interfaces.TaskManager;
import interfaces.service.EpicService;
import interfaces.service.SubtaskService;
import model.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import util.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryEpicServiceTest {
    EpicService epicService;
    SubtaskService subtaskService;

    @BeforeEach
    public void beforeEach() {
        TaskManager manager = new InMemoryTaskManager();

        epicService = manager.getEpicService();
        subtaskService = manager.getSubtaskService();
    }

    @Test
    public void testCreation() {
        // Arrange
        String name = "name";
        String description = "Lorem ipsum";

        // Act
        Epic epic = epicService.create(name, description);

        // Assert
        assertEquals(name, epic.getName());
        assertEquals(description, epic.getDescription());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertTrue(epic.getSubtaskIds().isEmpty());
    }

    @Test
    public void testUpdating() {
        // Arrange
        Epic epic = epicService.create("name", "lorem ipsum dollar");

        // Act
        epic.setName("new name");
        epic.setDescription("some text");

        Epic actualEpic = epicService.update(epic);

        // Assert
        assertSame(epic, actualEpic);
        assertEquals(epic, actualEpic);
    }
}