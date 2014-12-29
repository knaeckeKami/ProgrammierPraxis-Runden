/**
 * Created by mkamleithner on 12/29/14.
 */
public class FactoryException extends Exception {

    public FactoryException(
    ) {
        super(AsciiShop.ERRORS.INPUT_ERROR.toString());
    }

    public FactoryException(String message) {
        super(message);
    }
}
