import java.util.Scanner;

/**
 * Created by mkamleithner on 10/24/14.
 */
public class AsciiShop {

    public static void main(String[] args) {

        try {
            AsciiImage image;
            Scanner scanner = new Scanner(System.in);


            AsciiImageCommandBuilder builder = new AsciiImageCommandBuilder();
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
                    throw new AsciiShopException(ERRORS.INPUT_ERROR.toString());
                }

                //perform that operation on the image
                Object operationResult = operation.performTask(image);
                //if this operation had a result, save it
                if (operationResult != null) {
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
    public static interface AsciiImageOperation<T> {
        public T performTask(AsciiImage image);
    }

    /**
     * class to read commands, parse them and convert them to Operation-Objects
     */
    public static class AsciiImageCommandBuilder {


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
            } else throw new AsciiShopException(ERRORS.UNKNOWN_COMMAND.toString());
        }


    }

    public static class AsciiImageCreator implements AsciiImageOperation<AsciiImage> {

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
    }

    public static class AsciiImageLoader implements AsciiImageOperation<Void> {


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

    public static class AsciiClearTask implements AsciiImageOperation<Void> {
        public Void performTask(AsciiImage image) {
            image.clear();
            return null;
        }
    }

    public static class AsciiPrintTask implements AsciiImageOperation<Void> {
        public Void performTask(AsciiImage image) {
            System.out.println(image.toString());
            return null;
        }
    }

    public static class AsciiSymmetryChecker implements AsciiImageOperation<Boolean> {

        public Boolean performTask(AsciiImage image) {
            return image.isSymmetricHorizontal();
        }
    }

    public static class AsciiImageFiller implements AsciiImageOperation<Void> {


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

    public static class Transposer implements AsciiImageOperation<Void> {

        public Void performTask(AsciiImage image) {

            image.transpose();
            return null;
        }
    }

    public static class AsciiReplaceOperation implements AsciiImageOperation<Void> {

        char oldChar, newChar;

        public AsciiReplaceOperation(char oldChar, char newChar) {
            this.oldChar = oldChar;
            this.newChar = newChar;
        }

        @Override
        public Void performTask(AsciiImage image) {
            image.replace(oldChar, newChar);
            return null;
        }
    }

    public static class AsciiLineDrawer implements AsciiImageOperation<Void> {

        private int x0, x1, y0, y1;
        private char c;

        public AsciiLineDrawer(int x0, int y0, int x1, int y1, char c) {
            this.x0 = x0;
            this.x1 = x1;
            this.y0 = y0;
            this.y1 = y1;
            this.c = c;
        }

        @Override
        public Void performTask(AsciiImage image) {
            image.drawLine(x0, y0, x1, y1, c);
            return null;
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