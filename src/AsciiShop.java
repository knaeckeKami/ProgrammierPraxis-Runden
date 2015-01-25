import java.util.*;

/**
 * Created by mkamleithner on 10/24/14.
 */
public class AsciiShop {

    private static final HashMap<String, Factory> factories = new HashMap<String, Factory>();
    private static final AsciiStack stack = new AsciiStack();
    private static final Set<Class<? extends Operation>> undoableOperations = new HashSet<Class<? extends Operation>>();

    static {
        MetricSet<AsciiImage> saved = new MetricSet<AsciiImage>();
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
        factoryList.add(new HistogramFactory());
        factoryList.add(new SearchFactory(saved));
        factoryList.add(new SaveFactory(saved));
        factoryList.add(new PrintSavedFactory(saved));


        /**
         * here the factory-list gets converted to a HashMap
         * with the KeyWord as key.
         *
         * E.g. one entry in the map is "load" , <Instance of LoadFactory>
         */

        for (Factory factory : factoryList) {
            factories.put(factory.getKeyWord(), factory);
        }

        /**
         * build list of undoable operations
         */
        undoableOperations.add(BinaryOperation.class);
        undoableOperations.add(ClearOperation.class);
        undoableOperations.add(FillOperation.class);
        undoableOperations.add(LineOperation.class);
        undoableOperations.add(ReplaceOperation.class);
        undoableOperations.add(TransposeOperation.class);
        undoableOperations.add(MedianOperation.class);
        undoableOperations.add(AverageOperation.class);
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

            Factory cf = factories.get(scanner.next());
            if (cf == null || !(cf instanceof CreateFactory)) {
                throw new InputException(ERRORS.INPUT_ERROR.toString());
            }
            CreateOperation co = (CreateOperation) cf.create(scanner);
            image = co.execute(null);

            Operation operation;

            //read lines until no more lines are available
            while (scanner.hasNext()) {
                //get  factory-object from input-command
                Factory factory = factories.get(scanner.next());
                if (factory == null) {
                    throw new InputException(ERRORS.UNKNOWN_COMMAND.toString());
                }
                //build operation object
                operation = factory.create(scanner);
                //perform that operation on the image
                AsciiImage operationResult = operation.execute(image);
                // if the operation can be undone, save the old image on the stack
                if (undoableOperations.contains(operation.getClass())) {
                    stack.push(image);
                }

                //update the image
                image = operationResult;
            }

        } catch (OperationException ase) {
            //print error message
            System.out.println(ase.getMessage());
        } catch (InputException ip) {
            System.out.println(ip.getMessage());
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
     * class to undo the previous operation. needs an AsciiStack on creation
     */
    public static class UndoOperation implements Operation {

        private final AsciiStack stack;

        public UndoOperation(AsciiStack stack) {
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
                return image;
            } else {
                AsciiImage poppedImage = stack.pop();
                return poppedImage;
            }

        }
    }



    /**
     * custom exception for some input errors, which fit in no other
     * category (e.q. no input at all)
     */
    public static class InputException extends RuntimeException {

        public InputException() {
            this(AsciiShop.ERRORS.INPUT_ERROR.toString());
        }

        public InputException(String mesg) {
            super(mesg);
        }

    }

    /**
     * custom factory for creating AsciiPrintOperations
     */
    public static class PrintFactory implements Factory {

        public Operation create(Scanner scanner) throws FactoryException {
            return new AsciiPrintOperation();
        }

        public String getKeyWord() {
            return "print";
        }
    }

    /**
     * factory for generating AsciiUndoOperations
     */
    public static class UndoFactory implements Factory {

        private AsciiStack stack;

        public UndoFactory(AsciiStack stack) {
            this.stack = stack;
        }

        public Operation create(Scanner scanner) throws FactoryException {
            return new UndoOperation(stack);
        }

        public String getKeyWord() {
            return "undo";
        }
    }

    /**
     * factory for generating an histogram-operation
     */
    public static class HistogramFactory implements Factory {

        public Operation create(Scanner scanner) throws FactoryException {
            return new HistogramOperation();
        }

        public String getKeyWord() {
            return "histogram";
        }
    }

    /**
     * custom operation for printing a histogram of an AsciiImage
     */
    public static class HistogramOperation implements Operation {
        public AsciiImage execute(AsciiImage img) throws OperationException {
            new AsciiPrintOperation().execute(Histogram.getHistogram(img));
            return img;
        }
    }

    /**
     * custom operation for printing all saved AsciiImages
     */
    public static class PrintSavedOperation implements Operation {

        /**
         * reference to the set of saved images
         */
        private MetricSet<AsciiImage> saved;

        public PrintSavedOperation(MetricSet<AsciiImage> saved) {
            this.saved = saved;
        }

        public AsciiImage execute(AsciiImage noParamNeeded) throws OperationException {
            //print all images
            for (AsciiImage image : saved) {
                new AsciiPrintOperation().execute(image);
            }
            //print error message
            if (saved.isEmpty()) {
                System.out.println("NO SAVED IMAGES");
            }
            //return the untouched original image
            return noParamNeeded;
        }
    }

    public static class PrintSavedFactory implements Factory {

        private final MetricSet<AsciiImage> saved;

        public PrintSavedFactory(MetricSet<AsciiImage> saved) {
            this.saved = saved;
        }

        public Operation create(Scanner scanner) throws FactoryException {
            return new PrintSavedOperation(saved);
        }

        public String getKeyWord() {
            return "printsaved";
        }
    }


}