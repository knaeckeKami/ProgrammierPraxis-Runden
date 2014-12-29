import java.util.Scanner;

/**
 * Created by mkamleithner on 12/29/14.
 */
public class FilterFactory implements Factory {

    public Operation create(Scanner scanner) throws FactoryException {
        if (!scanner.hasNext("median\\s")) {
            throw new FactoryException();
        }
        if (!scanner.next().equals("median")) {
            throw new FactoryException();
        }
        return new MedianOperation();

    }

    public String getKeyWord() {
        return "filter";
    }
}
