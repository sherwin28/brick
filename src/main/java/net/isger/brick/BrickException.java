package net.isger.brick;

/**
 * 异常信息
 * 
 * @author issing
 * 
 */
public class BrickException extends RuntimeException {

    private static final long serialVersionUID = 7894106853811967221L;

    private int code;

    public BrickException() {
        super();
    }

    public BrickException(String message) {
        super(message);
    }

    public BrickException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrickException(Throwable cause) {
        super(cause);
    }

    public BrickException(int code) {
        this(code, null);
    }

    public BrickException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

}
