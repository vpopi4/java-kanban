package handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import util.TaskableValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

public abstract class BaseHttpHandler implements HttpHandler {
    protected final TaskManager manager;

    public BaseHttpHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET" -> handleGet(exchange);
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

    protected void handleGet(HttpExchange exchange) throws IOException {
        sendNotFound(exchange, "no such endpoint");
    }

    protected void handlePost(HttpExchange exchange) throws IOException {
        sendNotFound(exchange, "no such endpoint");
    }

    protected void handleDelete(HttpExchange exchange) throws IOException {
        sendNotFound(exchange, "no such endpoint");
    }

    protected Integer extractId(HttpExchange exchange) throws IllegalArgumentException {
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

    protected void sendOk(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 201, "");
    }

    protected void sendPayload(HttpExchange exchange, String payloadKey, String payloadValue) throws IOException {
        String responseBody = String.format("{\"%s\":%s}", payloadKey, payloadValue);

        sendResponse(exchange, 200, responseBody);
    }

    protected void sendBadRequest(HttpExchange exchange, String message) throws IOException {
        JsonObject json = new JsonObject();

        json.add("errorName", new JsonPrimitive("400 Bad Request"));
        json.add("message", new JsonPrimitive(message));

        sendResponse(exchange, 400, json.toString());
    }

    protected void sendNotFound(HttpExchange exchange, String message) throws IOException {
        JsonObject json = new JsonObject();

        json.add("errorName", new JsonPrimitive("404 Not Found"));
        json.add("message", new JsonPrimitive(message));

        sendResponse(exchange, 404, json.toString());
    }

    protected void sendIntersectionException(HttpExchange exchange, String message) throws IOException {
        JsonObject json = new JsonObject();

        json.add("errorName", new JsonPrimitive("406 Intersection Exception"));
        json.add("message", new JsonPrimitive(message));

        sendResponse(exchange, 406, json.toString());
    }

    protected void sendInternalServerError(HttpExchange exchange, String message) throws IOException {
        JsonObject json = new JsonObject();

        json.add("errorName", new JsonPrimitive("500 Internal Server Error"));
        json.add("message", new JsonPrimitive(message));

        sendResponse(exchange, 500, json.toString());
    }

    private void sendResponse(HttpExchange exchange, int code, String body) throws IOException {
        byte[] response = body.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(code, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }
}
