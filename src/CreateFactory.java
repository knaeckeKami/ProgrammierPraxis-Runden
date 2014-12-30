import java.util.NoSuchElementException;
import java.util.Scanner;

public class CreateFactory implements Factory {

    public Operation create(Scanner scanner) throws FactoryException {

        int width, height;
        String charset;
        try {
            width = scanner.nextInt();
            height = scanner.nextInt();
            charset = scanner.next();
        } catch (NoSuchElementException nse) {
            throw new FactoryException();
        }

        return new CreateOperation(width, height, charset);
    }

    public String getKeyWord() {
        return "create";
    }


}