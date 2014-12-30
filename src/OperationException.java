/**
 * Exception used for failures in Operations, e.g. caused by invalid parameters
 */
public class OperationException extends Exception {

    public OperationException() {
        this(AsciiShop.ERRORS.OPERATION_FAILED.toString());
    }

    public OperationException(String message) {
        super(message);
    }
}
