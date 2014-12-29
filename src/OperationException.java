/**
 * Created by mkamleithner on 12/27/14.
 */
public class OperationException extends Exception {

    public OperationException() {
        this(AsciiShop.ERRORS.OPERATION_FAILED.toString());
    }

    public OperationException(String message) {
        super(message);
    }
}
