import java.util.Scanner;

/**
 * Created by mkamleithner on 12/29/14.
 */
public interface Factory {

    public Operation create(Scanner scanner) throws FactoryException;

    public String getKeyWord();


}
