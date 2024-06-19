package handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import interfaces.TaskManager;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Managers;
import util.TaskConverter;
import util.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TasksHandlerTest {


    public static final LocalDateTime DATE_TIME = LocalDateTime.of(2024, 6, 19, 8, 0, 5);
    TaskManager manager;
    HttpTaskServer taskServer;
    Gson gson;

    public TasksHandlerTest() throws IOException {
        manager = new Managers().getInMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        gson = TaskConverter.gson;
    }

    @BeforeEach
    public void setUp() {
        manager.getTaskService().removeAll();
        manager.getEpicService().removeAll();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testCreationTask() throws IOException, InterruptedException {
        String taskJson = """
                {
                    task: {
                        "name": "Test Task",
                        "description": "Testing task creation",
                        "status": "IN_PROGRESS",
                        "duration": 300000,
                        "startTime": "2024-06-19T08:00:05"
                    }
                }
                """;

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        client.close();

        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getTaskService().getAll();

        assertNotNull(tasksFromManager, "Tasks not returned from storage");
        assertEquals(1, tasksFromManager.size(), "Incorrect number of tasks");

        Task task = tasksFromManager.get(0);
        assertEquals("Test Task", task.getName(), "Incorrect task name");
        assertEquals("Testing task creation", task.getDescription(), "Incorrect task description");
        assertEquals(TaskStatus.NEW, task.getStatus(), "Incorrect task status");
        assertEquals(Duration.ofMillis(300_000), task.getDuration(), "Incorrect task duration");
        assertEquals(DATE_TIME, task.getStartTime(), "Incorrect task startTime");
    }

    @Test
    public void testUpdatingTask() throws IOException, InterruptedException {
        manager.getTaskService().create(
                "Test Task",
                "Testing task updating",
                Duration.ofMillis(300_000),
                DATE_TIME
        );

        String taskJson = """
                {
                    task: {
                        "id": 0,
                        "name": "Changed name", // changed
                        "description": "Testing task updating",
                        "status": "DONE", // changed
                        "duration": 600000, // changed
                        "startTime": "2024-06-19T08:00:05"
                    }
                }
                """;

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        client.close();

        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getTaskService().getAll();

        assertNotNull(tasksFromManager, "Tasks not returned from storage");
        assertEquals(1, tasksFromManager.size(), "Incorrect number of tasks");

        Task task = tasksFromManager.get(0);

        assertEquals("Changed name", task.getName(), "Incorrect task name");
        assertEquals("Testing task updating", task.getDescription(), "Incorrect task description");
        assertEquals(TaskStatus.DONE, task.getStatus(), "Incorrect task status");
        assertEquals(Duration.ofMillis(600_000), task.getDuration(), "Incorrect task duration");
        assertEquals(DATE_TIME, task.getStartTime(), "Incorrect task startTime");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        Task task = manager.getTaskService().create(
                "Test Task",
                "Lorem ipsum dollar",
                Duration.ofMillis(300_000),
                DATE_TIME
        );
        manager.getTaskService().create(
                "Test Task",
                "Lorem ipsum dollar",
                Duration.ofMillis(300_000),
                DATE_TIME.plusDays(1)
        );
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        client.close();

        assertEquals(200, response.statusCode());

        List<Task> tasksFromResponse = JsonParser
                .parseString(response.body())
                .getAsJsonObject()
                .get("tasks")
                .getAsJsonArray()
                .asList()
                .stream()
                .map(JsonElement::getAsJsonObject)
                .map(Objects::toString)
                .map(TaskConverter::formJson)
                .filter(t -> t instanceof Task)
                .map(t -> (Task) t)
                .toList();

        assertNotNull(tasksFromResponse, "Tasks not returned");
        assertEquals(2, tasksFromResponse.size(), "Incorrect number of tasks");
    }


    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = manager.getTaskService().create(
                "Test Task",
                "Testing task retrieval",
                Duration.ofMillis(300_000),
                DATE_TIME
        );

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        client.close();

        assertEquals(200, response.statusCode());

        Task taskFromResponse = gson.fromJson(
                JsonParser
                        .parseString(response.body())
                        .getAsJsonObject()
                        .get("task")
                        .getAsJsonObject()
                        .toString()
                ,
                Task.class
        );

        assertNotNull(taskFromResponse, "Task not returned");
        assertEquals(task.getId(), taskFromResponse.getId(), "Incorrect task ID");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = manager.getTaskService().create(
                "Test Task",
                "Testing task deletion",
                Duration.ofMillis(300_000),
                DATE_TIME
        );

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        client.close();

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getTaskService().getAll();
        assertEquals(0, tasksFromManager.size(), "Task was not deleted");
    }
}
