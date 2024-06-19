package handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
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
