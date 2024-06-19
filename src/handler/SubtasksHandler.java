package handler;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import interfaces.model.Taskable;
import model.Subtask;
import util.TaskConverter;
import util.TaskableValidator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

public class SubtasksHandler extends BaseHttpHandler {
    private final TaskManager manager;

    public SubtasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            switch (method) {
                case "GET" -> {
                    if (path.matches("/subtasks/\\d+")) {
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
        List<Subtask> all = manager.getSubtaskService().getAll();

        sendPayload(exchange, "subtask", TaskConverter.toJson(all));
    }

    private void handleGetById(HttpExchange exchange) throws IOException {
        Subtask subtask = manager.getSubtaskService().get(extractId(exchange));

        sendPayload(exchange, "subtask", TaskConverter.toJson(subtask));
    }

    private void handlePost(HttpExchange exchange)
            throws IllegalArgumentException, NoSuchElementException, IOException {
        Subtask subtask = extractSubtask(exchange);

        if (subtask.getId() == null) {
            subtask = manager.getSubtaskService().create(
                    subtask.getEpicId(),
                    subtask.getName(),
                    subtask.getDescription(),
                    subtask.getDuration(),
                    subtask.getStartTime()
            );
        } else {
            subtask = manager.getSubtaskService().update(subtask);
        }

        sendPayload(exchange, "subtask", TaskConverter.toJson(subtask));
    }

    private Subtask extractSubtask(HttpExchange exchange) {
        try (InputStream requestBody = exchange.getRequestBody()) {
            String bodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

            Taskable taskable = TaskConverter.formJson(JsonParser
                    .parseString(bodyString)
                    .getAsJsonObject()
                    .get("subtask")
                    .getAsJsonObject()
                    .toString()
            );

            if (taskable instanceof Subtask) {
                return (Subtask) taskable;
            } else {
                throw new IllegalArgumentException("not a Subtask: " + taskable);
            }
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("json object was expected in request body");
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("could not resolve request body");
        }
    }

    private void handleDelete(HttpExchange exchange) throws IllegalArgumentException, IOException {
        manager.getSubtaskService().remove(extractId(exchange));
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
