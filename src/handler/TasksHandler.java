package handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import interfaces.model.Taskable;
import model.Task;
import util.TaskConverter;
import util.TaskableValidator;

import java.io.IOException;
import java.io.InputStreamReader;
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
        } catch (Exception e) {
            sendInternalServerError(exchange, "something went wrong");
            e.printStackTrace();
        }
    }

    private void handleGetAll(HttpExchange exchange) throws IOException {
        List<Task> all = manager.getTaskService().getAll();

        sendPayload(exchange, "tasks", TaskConverter.toJson(all));
    }

    private void handleGetById(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        Integer id = Integer.parseInt(pathParts[2]);

        try {
            Task task = manager.getTaskService().get(id);
            sendPayload(exchange, "task", TaskConverter.toJson(task));
        } catch (NoSuchElementException e) {
            sendNotFound(exchange, e.getMessage());
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        JsonObject json = JsonParser.parseReader(isr).getAsJsonObject();
        Taskable task = TaskConverter.formJson(json.get("task").getAsString());

        try {
            if (task.getId() == null) {
                task = manager.getTaskService().create(
                        task.getName(),
                        task.getDescription(),
                        task.getDuration(),
                        task.getStartTime()
                );
            } else if (task instanceof Task) {
                task = manager.getTaskService().update((Task) task);
            }
            sendPayload(exchange, "task", TaskConverter.toJson(task));
        } catch (NoSuchElementException e) {
            sendNotFound(exchange, e.getMessage());
        } catch (TaskableValidator.IntersectionException e) {
            sendIntersectionException(exchange, e.getMessage());
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(pathParts[2]);

        manager.getTaskService().remove(id);
        sendOk(exchange);
    }
}
