import java.util.Scanner;

/**
 * Created by mkamleithner on 12/29/14.
 *
 * Interface for reading the parameters for an Operation and creating
 * an instance of this Operation from the parameters.
 *
 *
 */
public interface Factory {

    /**
     * reads parameters from the given scanner and
     * builds the operation-instance from the parameters
     *
     * @param scanner
     * @return
     * @throws FactoryException if wrong parameters are read from the scanner
     */
    public Operation create(Scanner scanner) throws FactoryException;

    /**
     * returns the keyword belonging to the Operation
     * @return
     */
    public String getKeyWord();


}
