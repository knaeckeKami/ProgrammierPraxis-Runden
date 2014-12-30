import java.util.Scanner;

/**
 * Created by mkamleithner on 12/29/14.
 */
public class FilterFactory implements Factory {

    public Operation create(Scanner scanner) throws FactoryException {
        if (!scanner.hasNext()) {
            throw new FactoryException();
        }
        String filter = scanner.next();
        if (filter.equals("median")) {
            return new MedianOperation();
        } else if (filter.equals("average")) {
            return new AverageOperation();
        } else throw new FactoryException();

    }

    public String getKeyWord() {
        return "filter";
    }
}
