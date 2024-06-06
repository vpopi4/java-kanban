package util;

public class ManagerSaveException extends RuntimeException {
    private Exception exception;

    public ManagerSaveException(Exception exception) {
        this.exception = exception;
    }
}
