import java.util.*;

/**
 * Created by mkamleithner on 10/24/14.
 */
public class AsciiShop {

    private static final HashMap<String, Factory> factories = new HashMap<String, Factory>();
    private static final AsciiStack stack = new AsciiStack();

    static {
        List<Factory> factoryList = new ArrayList<Factory>();
        /**
         * In case of implementing new commands, just
         * add your factory to this this here.
         */
        factoryList.add(new BinaryFactory());
        factoryList.add(new ClearFactory());
        factoryList.add(new FilterFactory());
        factoryList.add(new LoadFactory());
        factoryList.add(new ReplaceFactory());
        factoryList.add(new PrintFactory());
        factoryList.add(new CreateFactory());
        factoryList.add(new UndoFactory(stack));

        /**
         * here the factory-list gets converted to a HashMap
         * with the KeyWord as key.
         *
         * E.g. one entry in the map is "load" , <Instance of LoadFactory>
         */

        for (Factory factory : factoryList) {
            factories.put(factory.getKeyWord(), factory);
        }
    }


    public static void main(String[] args) {

        try {
            //reference to the currently active image
            AsciiImage image;
            Scanner scanner = new Scanner(System.in);
            //initialise the image
            if (!scanner.hasNext()) {
                throw new InputException();
            }
            CreateFactory cf = (CreateFactory) factories.get(scanner.next());
            if (cf == null) {
                throw new InputException(ERRORS.INPUT_ERROR.toString());
            }
            CreateOperation co = (CreateOperation) cf.create(scanner);
            image = co.execute(null);

            Operation operation;

            //read lines until no more lines are available
            while (scanner.hasNextLine()) {
                //build operation-object from input-command
                Factory factory = factories.get(scanner.next());
                if (factory == null) {
                    throw new InputException(ERRORS.UNKNOWN_COMMAND.toString());
                }
                operation = factory.create(scanner);
                //image has already been created, must not be created again.
                if (operation instanceof CreateOperation) {
                    throw new InputException();
                }
                //perform that operation on the image
                AsciiImage operationResult = operation.execute(image);
                if (operationResult != image && !(operation instanceof AsciiUndoOperation)) {
                    stack.push(image);
                }
                image = operationResult;
            }

        } catch (OperationException ase) {
            //print error message
            System.out.println(ase.getMessage());
        } catch (InputException ip) {
            System.out.println(ip.getMessage());
            ip.printStackTrace();
        } catch (FactoryException f) {
            System.out.println(f.getMessage());
        }
    }

    /**
     * generic error messages
     */
    public enum ERRORS {

        INPUT_ERROR("INPUT MISMATCH"),
        UNKNOWN_COMMAND("UNKNOWN COMMAND"),
        OPERATION_FAILED("OPERATION FAILED");

        private String ERROR_NAME;

        private ERRORS(String name) {
            this.ERROR_NAME = name;
        }

        public String toString() {
            return this.ERROR_NAME;
        }
    }


    /**
     * class used to implement the command to print the image
     */
    public static class AsciiPrintOperation implements Operation {
        public AsciiImage execute(AsciiImage image) throws OperationException {
            //image must be transposed before it can be printed
            System.out.println(new TransposeOperation().execute(image));
            return image;

        }

    }


    /**
     * class to undo the previous operation. needs an asciistack on creation
     */
    public static class AsciiUndoOperation implements Operation {

        private AsciiStack stack;

        public AsciiUndoOperation(AsciiStack stack) {
            this.stack = stack;
        }


        /**
         * UNDOs the previous operation. Iff no operation  can be undone, then null is returned an
         * "STACK EMPTY" is printed on System.out.
         * Otherwise, the state of the stack if printed (without the topmost image) and the topmost image is returned
         *
         * @param image must be null
         * @return the last AsciiImage or null
         */
        public AsciiImage execute(AsciiImage image) {
            if (stack.empty()) {
                System.out.println("STACK EMPTY");
                return null;
            } else {
                AsciiImage poppedImage = stack.pop();
                return poppedImage;
            }

        }
    }

    /**
     * custom operation the create a new image
     * Essentially a wrapper around the constructor.
     */
    public static class CreateOperation implements Operation {

        private final int width, height;
        private String charset;

        public CreateOperation(int width, int height, String charset) {
            this.height = height;
            this.width = width;
            this.charset = charset;

        }

        /**
         * @param image must be null
         * @return new AsciiImage
         */
        public AsciiImage execute(AsciiImage image) {

            if (image != null) {
                return null;
            }

            return new AsciiImage(width, height, charset);
        }

    }

    /**
     * Created by mkamleithner on 12/28/14.
     */
    public static class InputException extends RuntimeException {

        public InputException() {
            this(AsciiShop.ERRORS.INPUT_ERROR.toString());
        }

        public InputException(String mesg) {
            super(mesg);
        }

    }

    public static class PrintFactory implements Factory {

        public Operation create(Scanner scanner) throws FactoryException {
            return new AsciiPrintOperation();
        }

        public String getKeyWord() {
            return "print";
        }
    }

    public static class CreateFactory implements Factory {

        public Operation create(Scanner scanner) throws FactoryException {

            int width, height;
            String charset;
            try {
                width = scanner.nextInt();
                height = scanner.nextInt();
                charset = scanner.next();
            } catch (NoSuchElementException nse) {
                throw error();
            }

            return new CreateOperation(width, height, charset);
        }

        public String getKeyWord() {
            return "create";
        }

        private FactoryException error() throws FactoryException {
            return new FactoryException("illegal input!");
        }

    }

    public static class UndoFactory implements Factory {

        private AsciiStack stack;

        public UndoFactory(AsciiStack stack) {
            this.stack = stack;
        }

        public Operation create(Scanner scanner) throws FactoryException {
            return new AsciiUndoOperation(stack);
        }

        public String getKeyWord() {
            return "undo";
        }
    }


}