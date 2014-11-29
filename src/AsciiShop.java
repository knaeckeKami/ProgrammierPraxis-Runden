import java.util.Scanner;

/**
 * Created by mkamleithner on 10/24/14.
 */
public class AsciiShop {

    private static final int STACK_INCREMENT_SIZE = 3;

    public static void main(String[] args) {

        try {
            AsciiImage image;
            AsciiStack stack = new AsciiStack(STACK_INCREMENT_SIZE);
            Scanner scanner = new Scanner(System.in);
            AsciiImageCommandBuilder builder = new AsciiImageCommandBuilder(stack);
            //initialise the image
            AsciiImageOperation operation = builder.buildOperation(scanner);
            if (!(operation instanceof AsciiImageCreator)) {
                throw new AsciiShopException(ERRORS.INPUT_ERROR.toString());
            }
            image = ((AsciiImageCreator) operation).performTask(null);

            //read lines until no more lines are available
            while (scanner.hasNextLine()) {
                //build operation-object from input-command
                operation = builder.buildOperation(scanner);
                //image has already been created, must not be created again.
                if (operation instanceof AsciiImageCreator) {
                    throw new AsciiShopException(ERRORS.UNKNOWN_COMMAND.toString());
                }

                if (operation.hasSideEffectsOnImage()) {
                    stack.push(image);
                    image = new AsciiImage(image);
                }
                //perform that operation on the image
                Object operationResult = operation.performTask(image);
                if (operationResult instanceof AsciiImage) {
                    image = (AsciiImage) operationResult;
                }
                //if this operation had a result, save it
                else if (operation.hasResult()) {
                    System.out.println(operationResult);
                }
            }

        } catch (AsciiShopException ase) {
            //print error message
            System.out.println(ase.getMessage());
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
     * interface for an operation to perform on an ascii-image
     *
     * @param <T> Result of the operation. May be Void.
     */
    public static abstract class AsciiImageOperation<T> {

        /**
         * returns true, iff the state of the image is changed when performTask is called.
         * returns false otherwise
         *
         * @return
         */
        public boolean hasSideEffectsOnImage() {
            return true;
        }

        /**
         * performs an operation on the given image and returns a value (or null)
         *
         * @param image the image, on which the operation is performed. might be null on specific operations like image creation
         * @return
         */
        public abstract T performTask(AsciiImage image);

        public boolean hasResult() {
            return !hasSideEffectsOnImage();
        }
    }

    /**
     * class to read commands, parse them and convert them to Operation-Objects
     */
    public static class AsciiImageCommandBuilder {

        //the stack needed for the undo operation
        private AsciiStack stack;

        public AsciiImageCommandBuilder(AsciiStack stack) {
            this.stack = stack;
        }

        public AsciiImageOperation buildOperation(Scanner input) {
            String line = input.nextLine();
            String[] token = line.split(" ");
            if ("create".equals(token[0])) {
                if (token.length != 3) {
                    throw new AsciiShopException(ERRORS.INPUT_ERROR.toString());
                }
                int width, height;
                try {
                    width = Integer.parseInt(token[1]);
                    height = Integer.parseInt(token[2]);
                } catch (NumberFormatException nfe) {
                    throw new AsciiShopException(ERRORS.INPUT_ERROR.toString());
                }
                return new AsciiImageCreator(width, height);
            } else if ("load".equals(token[0])) {
                if (token.length != 2) {
                    throw new AsciiShopException(ERRORS.INPUT_ERROR.toString());
                }
                return new AsciiImageLoader(token[1], input);
            } else if ("print".equals(token[0])) {
                return new AsciiPrintTask();
            } else if ("clear".equals(token[0])) {
                return new AsciiClearTask();
            } else if ("replace".equals(token[0])) {
                if (token.length != 3 && (token[1].length() != 1 || token[2].length() != 1)) {
                    throw new AsciiShopException(ERRORS.INPUT_ERROR.toString());
                }
                char oldChar = token[1].charAt(0);
                char newChar = token[2].charAt(0);

                return new AsciiReplaceOperation(oldChar, newChar);


            } else if ("line".equals(token[0])) {
                if (token.length != 6) {
                    throw new AsciiShopException(ERRORS.INPUT_ERROR.toString());
                }
                int x0, y0, x1, y1;
                char c;
                //try to parse the parameters, throw an exception otherwise
                //x,y must be integers, checking of the boundaries is up to the AsciiImage itself
                try {
                    x0 = Integer.parseInt(token[1]);
                    y0 = Integer.parseInt(token[2]);
                    x1 = Integer.parseInt(token[3]);
                    y1 = Integer.parseInt(token[4]);
                } catch (NumberFormatException nfe) {
                    throw new AsciiShopException(AsciiShop.ERRORS.INPUT_ERROR.toString());
                }
                //c must be exactly one character
                if (token[5].length() != 1) {
                    throw new AsciiShopException(AsciiShop.ERRORS.INPUT_ERROR.toString());
                }
                c = token[5].charAt(0);
                return new AsciiLineDrawer(x0, y0, x1, y1, c);
            }
            if ("transpose".equals(token[0])) {
                if (token.length != 1) {
                    throw new AsciiShopException(AsciiShop.ERRORS.INPUT_ERROR.toString());
                }
                return new Transposer();
            } else if ("fill".equals(token[0])) {
                if (token.length != 4) {
                    throw new AsciiShopException(AsciiShop.ERRORS.INPUT_ERROR.toString());
                }
                //parameters for the fill command
                int x, y;
                char c;
                //try to parse the parameters, throw an exception otherwise
                //x,y must be integers, checking of the boundaries is up to the AsciiImage itself
                try {
                    x = Integer.parseInt(token[1]);
                    y = Integer.parseInt(token[2]);
                } catch (NumberFormatException nfe) {
                    throw new AsciiShopException(AsciiShop.ERRORS.INPUT_ERROR.toString());
                }
                //c must be exactly one character
                if (token[3].length() != 1) {
                    throw new AsciiShopException(AsciiShop.ERRORS.INPUT_ERROR.toString());
                }
                c = token[3].charAt(0);
                return new AsciiImageFiller(x, y, c);
            } else if ("symmetric-h".equals(token[0])) {
                if (token.length != 1) {
                    throw new AsciiShopException(AsciiShop.ERRORS.INPUT_ERROR.toString());
                }
                return new AsciiSymmetryChecker();
            } else if ("undo".equals(token[0])) {
                if (token.length != 1) {
                    throw new AsciiShopException(AsciiShop.ERRORS.INPUT_ERROR.toString());
                }
                return new AsciiUndoOperation(stack);
            } else if ("grow".equals(token[0])) {
                if (token.length != 2 && token[1].length() != 1) {
                    throw new AsciiShopException(AsciiShop.ERRORS.INPUT_ERROR.toString());
                }
                return new AsciiGrowOperation(token[1].charAt(0));
            } else if ("centroid".equals(token[0])) {
                if (token.length != 2 && token[1].length() != 1) {
                    throw new AsciiShopException(AsciiShop.ERRORS.INPUT_ERROR.toString());
                }
                return new AsciiCentroidOperation(token[1].charAt(0));
            }
            //no valid command was given : throw an exception
            throw new AsciiShopException(ERRORS.UNKNOWN_COMMAND.toString());
        }

    }

    /**
     * class used to implement the command to grow a region for a specific character
     */
    public static class AsciiGrowOperation extends AsciiImageOperation<Void> {

        private char charToGrow;

        public AsciiGrowOperation(char c) {
            this.charToGrow = c;
        }

        public Void performTask(AsciiImage image) {
            image.growRegion(charToGrow);
            return null;
        }
    }

    /**
     * class used to implement the command to calculate the centroid of a character in an image
     */
    public static class AsciiCentroidOperation extends AsciiImageOperation<AsciiPoint> {

        private char characterToCalculateCentroidOf;

        public AsciiCentroidOperation(char c) {
            this.characterToCalculateCentroidOf = c;
        }

        public boolean hasSideEffectsOnImage() {
            return false;
        }

        public AsciiPoint performTask(AsciiImage image) {
            return image.getCentroid(characterToCalculateCentroidOf);
        }
    }

    /**
     * class used to implement the command to create an image
     */
    public static class AsciiImageCreator extends AsciiImageOperation<AsciiImage> {

        private final int width, height;

        public AsciiImageCreator(int width, int height) {
            this.height = height;
            this.width = width;
        }

        public AsciiImage performTask(AsciiImage image) {

            if (image != null) {
                return null;
            }

            return new AsciiImage(width, height);
        }

        /**
         * is creates a new object, but does not touch an existing one -> no side effects
         *
         * @return
         */
        public boolean hasSideEffectsOnImage() {
            return false;
        }
    }

    /**
     * class used to implement the command to load the image
     **/

    public static class AsciiImageLoader extends AsciiImageOperation<Void> {


        private final String eof;
        private final Scanner scanner;

        public AsciiImageLoader(String eof, Scanner input) {
            this.eof = eof;
            this.scanner = input;
        }

        public Void performTask(AsciiImage image) {

            int width = image.getWidth();
            int height = image.getHeight();

            for (int i = 0; i < height; i++) {
                if (!scanner.hasNextLine()) {
                    throw new AsciiShopException(ERRORS.INPUT_ERROR.toString());
                }
                String line = scanner.nextLine();
                if (line.length() != width) {
                    throw new AsciiShopException(ERRORS.INPUT_ERROR.toString());
                }
                for (int j = 0; j < line.length(); j++) {
                    image.setPixel(j, i, line.charAt(j));
                }
            }

            if (!scanner.hasNextLine() || !scanner.nextLine().equals(eof)) {
                throw new AsciiShopException(ERRORS.INPUT_ERROR.toString());
            }


            return null;
        }

    }

    /**
     * class used to implement the command to clear the image
     */
    public static class AsciiClearTask extends AsciiImageOperation<Void> {
        public Void performTask(AsciiImage image) {
            image.clear();
            return null;
        }

    }

    /**
     * class used to implement the command to print the image
     */
    public static class AsciiPrintTask extends AsciiImageOperation<String> {
        public String performTask(AsciiImage image) {
            return image.toString();
        }

        public boolean hasSideEffectsOnImage() {
            return false;
        }

    }

    /**
     * class used to implement the command symmetric-h
     */

    public static class AsciiSymmetryChecker extends AsciiImageOperation<Boolean> {

        public Boolean performTask(AsciiImage image) {
            return image.isSymmetricHorizontal();
        }

        public boolean hasSideEffectsOnImage() {
            return false;
        }

    }

    /**
     * class used to implement the command to fill an image
     */
    public static class AsciiImageFiller extends AsciiImageOperation<Void> {


        private int x, y;
        private char character;


        public AsciiImageFiller(int x, int y, char character) {
            this.x = x;
            this.y = y;
            this.character = character;
        }

        public Void performTask(AsciiImage image) {
            image.fill(x, y, character);
            return null;
        }


    }

    /**
     * class used to implement the command to transpose an image
     */
    public static class Transposer extends AsciiImageOperation<Void> {

        public Void performTask(AsciiImage image) {

            image.transpose();
            return null;
        }

    }

    /**
     * class used to implement the command to replace a character
     */
    public static class AsciiReplaceOperation extends AsciiImageOperation<Void> {

        char oldChar, newChar;

        public AsciiReplaceOperation(char oldChar, char newChar) {
            this.oldChar = oldChar;
            this.newChar = newChar;
        }

        public Void performTask(AsciiImage image) {
            image.replace(oldChar, newChar);
            return null;
        }


    }

    /**
     * class used to implement the command to draw a line
     */
    public static class AsciiLineDrawer extends AsciiImageOperation<Void> {

        private int x0, x1, y0, y1;
        private char c;

        public AsciiLineDrawer(int x0, int y0, int x1, int y1, char c) {
            this.x0 = x0;
            this.x1 = x1;
            this.y0 = y0;
            this.y1 = y1;
            this.c = c;
        }


        public Void performTask(AsciiImage image) {
            image.drawLine(x0, y0, x1, y1, c);
            return null;
        }

    }

    /**
     * class to undo the previous operation. needs an asciistack on creation
     */
    public static class AsciiUndoOperation extends AsciiImageOperation<AsciiImage> {

        private AsciiStack stack;

        public AsciiUndoOperation(AsciiStack stack) {
            this.stack = stack;
        }

        public boolean hasSideEffectsOnImage() {
            return false;
        }

        @Override
        public boolean hasResult() {
            return false;
        }

        /**
         *  UNDOs the previous operation. Iff no operation  can be undone, then null is returned an
         *  "STACK EMPTY" is printed on System.out.
         *  Otherwise, the state of the stack if printed (without the topmost image) and the topmost image is returned
         * @param image must be null
         * @return the last AsciiImage or null
         */
        public AsciiImage performTask(AsciiImage image) {
            if (stack.empty()) {
                System.out.println("STACK EMPTY");
                return null;
            } else {
                AsciiImage poppedImage = stack.pop();
                System.out.println("STACK USAGE " + stack.size() + "/" + stack.capacity());
                return poppedImage;
            }

        }
    }

    /**
     * own exception used for this project
     */
    public static class AsciiShopException extends RuntimeException {

        public AsciiShopException() {

        }

        public AsciiShopException(String message) {
            super(message);
        }
    }

}