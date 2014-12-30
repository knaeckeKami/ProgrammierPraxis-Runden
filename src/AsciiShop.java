import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by mkamleithner on 10/24/14.
 */
public class AsciiShop {

    private static final HashMap<String, Factory> factories = new HashMap<String, Factory>();
    private static final AsciiStack stack = new AsciiStack();

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
                //build operation-object from input-command
                Factory factory = factories.get(scanner.next());
                if (factory == null) {
                    throw new InputException(ERRORS.UNKNOWN_COMMAND.toString());
                }
                operation = factory.create(scanner);
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

    public static class HistogramFactory implements Factory {

        public Operation create(Scanner scanner) throws FactoryException {
            return new HistogramOperation();
        }

        public String getKeyWord() {
            return "histogram";
        }
    }

    public static class HistogramOperation implements Operation {
        public AsciiImage execute(AsciiImage img) throws OperationException {
            new AsciiPrintOperation().execute(Histogram.getHistogram(img));
            return img;
        }
    }

    public static class PrintSavedOperation implements Operation {

        private MetricSet<AsciiImage> saved;

        public PrintSavedOperation(MetricSet<AsciiImage> saved) {
            this.saved = saved;
        }

        public AsciiImage execute(AsciiImage noParamNeeded) throws OperationException {
            for (AsciiImage image : saved) {
                new AsciiPrintOperation().execute(image);
            }
            if (saved.isEmpty()) {
                System.out.println("NO SAVED IMAGES");
            }
            return noParamNeeded;
        }
    }

    public static class PrintSavedFactory implements Factory {

        private MetricSet<AsciiImage> saved;

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