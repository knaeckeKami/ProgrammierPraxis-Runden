import java.util.Scanner;

/**
 * Created by mkamleithner on 10/24/14.
 */
public class AsciiShop {


    public static void main(String[] args) {

        try {
            //reference to the currently active image
            AsciiImage image;
            AsciiStack stack = new AsciiStack();
            Scanner scanner = new Scanner(System.in);
            AsciiImageCommandBuilder builder = new AsciiImageCommandBuilder(stack);
            //initialise the image
            Operation operation = builder.buildOperation(scanner);
            if (!(operation instanceof CreateOperation)) {
                throw new InputException();
            }
            image = ((CreateOperation) operation).execute(null);

            //read lines until no more lines are available
            while (scanner.hasNextLine()) {
                //build operation-object from input-command
                operation = builder.buildOperation(scanner);
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
     * class to read commands, parse them and convert them to Operation-Objects
     */
    public static class AsciiImageCommandBuilder {

        //the stack needed for the undo operation
        private AsciiStack stack;

        public AsciiImageCommandBuilder(AsciiStack stack) {
            this.stack = stack;
        }

        public Operation buildOperation(Scanner input) {
            String line = input.nextLine();
            String[] token = line.split(" ");
            if ("create".equals(token[0])) {
                if (token.length != 4) {
                    throw new InputException();
                }
                int width, height;
                String charset;
                try {
                    width = Integer.parseInt(token[1]);
                    height = Integer.parseInt(token[2]);
                    charset = token[3];
                } catch (NumberFormatException nfe) {
                    throw new InputException();
                }
                return new CreateOperation(width, height, charset);
            } else if ("load".equals(token[0])) {
                if (token.length != 2) {
                    throw new InputException();
                }
                String eof = token[1];
                String data = "";
                while (input.hasNextLine() && (line = input.nextLine()) != null && !eof.equals(line)) {
                    data += line + "\n";
                }

                return new LoadOperation(data);
            } else if ("print".equals(token[0])) {
                return new AsciiPrintOperation();
            } else if ("clear".equals(token[0])) {
                return new ClearOperation();
            } else if ("replace".equals(token[0])) {
                if (token.length != 3 && (token[1].length() != 1 || token[2].length() != 1)) {
                    throw new InputException();
                }
                char oldChar = token[1].charAt(0);
                char newChar = token[2].charAt(0);

                return new ReplaceOperation(oldChar, newChar);


            } else if ("line".equals(token[0])) {
                if (token.length != 6) {
                    throw new InputException();
                }
                int x0, y0, x1, y1;
                char c;
                //try to parse the parameters, throw anRuntimeException otherwise
                //x,y must be integers, checking of the boundaries is up to the AsciiImage itself
                try {
                    x0 = Integer.parseInt(token[1]);
                    y0 = Integer.parseInt(token[2]);
                    x1 = Integer.parseInt(token[3]);
                    y1 = Integer.parseInt(token[4]);
                } catch (NumberFormatException nfe) {
                    throw new InputException();
                }
                //c must be exactly one character
                if (token[5].length() != 1) {
                    throw new InputException();
                }
                c = token[5].charAt(0);
                return new LineOperation(x0, y0, x1, y1, c);
            }
            if ("transpose".equals(token[0])) {
                if (token.length != 1) {
                    throw new InputException();
                }
                return new TransposeOperation();
            } else if ("fill".equals(token[0])) {
                if (token.length != 4) {
                    throw new InputException();
                }
                //parameters for the fill command
                int x, y;
                char c;
                //try to parse the parameters, throw anRuntimeException otherwise
                //x,y must be integers, checking of the boundaries is up to the AsciiImage itself
                try {
                    x = Integer.parseInt(token[1]);
                    y = Integer.parseInt(token[2]);
                } catch (NumberFormatException nfe) {
                    throw new InputException();
                }
                //c must be exactly one character
                if (token[3].length() != 1) {
                    throw new InputException();
                }
                c = token[3].charAt(0);
                return new FillOperation(x, y, c);
            } else if ("undo".equals(token[0])) {
                if (token.length != 1) {
                    throw new InputException();
                }
                return new AsciiUndoOperation(stack);
            } else if ("grow".equals(token[0])) {
                if (token.length != 2 && token[1].length() != 1) {
                    throw new InputException();
                }
                return new GrowRegionOperation(token[1].charAt(0));
            } else if ("filter".equals(token[0])) {
                if (token.length != 2 || !token[1].equals("median")) {
                    throw new InputException();
                }
                return new MedianOperation();
            } else {
                //no valid command was given : throw anRuntimeException
                throw new InputException(ERRORS.UNKNOWN_COMMAND.toString());
            }
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
         *  UNDOs the previous operation. Iff no operation  can be undone, then null is returned an
         *  "STACK EMPTY" is printed on System.out.
         *  Otherwise, the state of the stack if printed (without the topmost image) and the topmost image is returned
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


}