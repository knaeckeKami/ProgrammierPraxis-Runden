/**
 * Exception used for input errors in Factories.
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
