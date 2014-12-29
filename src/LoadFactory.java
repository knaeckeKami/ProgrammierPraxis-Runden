import java.util.Scanner;

/**
 * Created by mkamleithner on 12/29/14.
 */
public class LoadFactory implements Factory {

    public Operation create(Scanner input) throws FactoryException {
        if (!input.hasNext()) {
            throw new FactoryException();
        }

        String eof = input.next();
        StringBuilder data = new StringBuilder();
        String line;
        while (input.hasNextLine() && (line = input.nextLine()) != null && !eof.equals(line)) {
            data.append(line).append("\n");
        }

        return new LoadOperation(data.toString());
    }

    public String getKeyWord() {
        return "load";
    }
}
