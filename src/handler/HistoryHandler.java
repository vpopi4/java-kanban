package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import util.TaskConverter;

import java.io.IOException;
import java.util.NoSuchElementException;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager manager;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET" -> handleGet(exchange);
                case null, default -> sendNotFound(exchange, "no such endpoint");
            }
        } catch (NoSuchElementException e) {
            sendNotFound(exchange, e.getMessage());
        } catch (IllegalArgumentException e) {
            sendBadRequest(exchange, e.getMessage());
        } catch (Exception e) {
            sendInternalServerError(exchange, "something went wrong");
            e.printStackTrace();
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        sendPayload(exchange, "history", TaskConverter.gson.toJson(manager.getHistory()));
    }
}
