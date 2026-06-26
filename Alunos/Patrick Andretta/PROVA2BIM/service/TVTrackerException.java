package service;

public class TVTrackerException extends Exception {
    private final int code;

    public TVTrackerException(String message, int code) {
        super(message);
        this.code = code;
    }

    public TVTrackerException(String message) {
        this(message, 0);
    }

    public int getCode() {
        return code;
    }
}
