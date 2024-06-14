package util;

public class ManagerSaveException extends RuntimeException {
    private final Exception exception;

    public ManagerSaveException(Exception exception) {
        this.exception = exception;
    }
}
