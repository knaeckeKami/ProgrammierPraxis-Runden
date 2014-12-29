import java.util.Scanner;

/**
 * Created by mkamleithner on 12/29/14.
 */
public class ClearFactory implements Factory {

    public Operation create(Scanner scanner) throws FactoryException {
        return new ClearOperation();
    }

    public String getKeyWord() {
        return "clear";
    }

}
