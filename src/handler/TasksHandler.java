package handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import interfaces.model.Taskable;
import model.Task;
import util.TaskConverter;
import util.TaskableValidator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

public class TasksHandler extends BaseHttpHandler {
    private final TaskManager manager;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            switch (method) {
                case "GET" -> {
                    if (path.matches("/tasks/\\d+")) {
                        handleGetById(exchange);
                    } else {
                        handleGetAll(exchange);
                    }
                }
                case "POST" -> handlePost(exchange);
                case "DELETE" -> handleDelete(exchange);
                case null, default -> sendNotFound(exchange, "no such endpoint");
            }
        } catch (NoSuchElementException e) {
            sendNotFound(exchange, e.getMessage());
        } catch (TaskableValidator.IntersectionException e) {
            sendIntersectionException(exchange, e.getMessage());
        } catch (IllegalArgumentException e) {
            sendBadRequest(exchange, e.getMessage());
        } catch (Exception e) {
            sendInternalServerError(exchange, "something went wrong");
            e.printStackTrace();
        }
    }

    private void handleGetAll(HttpExchange exchange) throws IOException {
        List<Task> all = manager.getTaskService().getAll();

        sendPayload(exchange, "tasks", TaskConverter.toJson(all));
    }

    private void handleGetById(HttpExchange exchange)
            throws IllegalArgumentException, NoSuchElementException, IOException {
        Task task = manager.getTaskService().get(extractId(exchange));

        sendPayload(exchange, "task", TaskConverter.toJson(task));
    }

    private void handlePost(HttpExchange exchange)
            throws IllegalArgumentException, NoSuchElementException, IOException {
        Taskable task;

        try (InputStream requestBody = exchange.getRequestBody()) {
            String bodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
            JsonObject bodyJson = JsonParser.parseString(bodyString).getAsJsonObject();

            task = TaskConverter.formJson(bodyJson.get("task").getAsString());
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("json object was expected in request body");
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("could not resolve request body");
        }

        if (task.getId() == null) {
            task = manager.getTaskService().create(
                    task.getName(),
                    task.getDescription(),
                    task.getDuration(),
                    task.getStartTime()
            );
        } else if (task instanceof Task) {
            task = manager.getTaskService().update((Task) task);
        } else {
            throw new IllegalStateException("not a Task: " + task);
        }

        sendPayload(exchange, "task", TaskConverter.toJson(task));
    }

    private void handleDelete(HttpExchange exchange) throws IllegalArgumentException, IOException {
        manager.getTaskService().remove(extractId(exchange));
        sendOk(exchange);
    }

    private Integer extractId(HttpExchange exchange) throws IllegalArgumentException {
        try {
            String secondInPath = exchange
                    .getRequestURI()
                    .getPath()
                    .split("/")
                    [2];
            return Integer.parseInt(secondInPath);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new IllegalArgumentException("id must be integer");
        }
    }
}
