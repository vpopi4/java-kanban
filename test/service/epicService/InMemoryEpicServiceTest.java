package service.epicService;

import interfaces.service.EpicService;
import interfaces.service.SubtaskService;
import model.Epic;
import model.TaskCreationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryEpicRepository;
import repository.InMemorySubtaskRepository;
import service.subtaskService.InMemorySubtaskService;
import util.IdGenerator;
import util.InMemoryHistoryManager;
import util.TaskManagerConfig;
import util.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryEpicServiceTest {
    EpicService epicService;
    SubtaskService subtaskService;

    @BeforeEach
    public void beforeEach() {
        TaskManagerConfig config = new TaskManagerConfig(
                null,
                new InMemoryEpicRepository(),
                new InMemorySubtaskRepository(),
                new IdGenerator(),
                new InMemoryHistoryManager()
        );

        epicService = new InMemoryEpicService(config);
        subtaskService = new InMemorySubtaskService(config);
    }

    @Test
    public void testCreation() {
        // Arrange
        TaskCreationData data = new TaskCreationData("name", "Lorem ipsum");

        // Act
        Epic epic = epicService.create(data);

        // Assert
        assertEquals(data.getName(), epic.getName());
        assertEquals(data.getDescription(), epic.getDescription());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertTrue(epic.getSubtasks().isEmpty());
    }

    @Test
    public void testUpdating() {
        // Arrange
        Epic epic = epicService.create(new TaskCreationData("name", "lorem ipsum dollar"));
        TaskCreationData data = new TaskCreationData(
                "new name",
                "some text"
        );

        // Act
        Epic actualEpic = epicService.update(epic.getId(), data);

        // Assert
        assertSame(epic, actualEpic);
        assertEquals(epic, actualEpic);
    }
}