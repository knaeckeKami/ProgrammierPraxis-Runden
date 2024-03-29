import java.util.Scanner;

/**
 * Created by mkamleithner on 12/29/14.
 */
public class BinaryFactory implements Factory {

    /**
     * returns a BinaryOperation
     *
     * @param scanner reads a single char, which will be the threshold for
     *                the BinaryOperation
     * @return
     * @throws FactoryException
     */
    public BinaryOperation create(Scanner scanner) throws FactoryException {
        char threshold;
        if (!scanner.hasNext()) {
            throw new FactoryException();
        }
        String next = scanner.next();
        if (next.length() != 1) {
            throw new FactoryException();
        }
        threshold = next.charAt(0);
        return new BinaryOperation(threshold);

    }

    public String getKeyWord() {
        return "binary";
    }
}
