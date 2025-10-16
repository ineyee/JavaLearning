package exception;

public class ServiceException extends Exception {
    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    private final Integer code;

    public int getCode() {
        return code;
    }
}
