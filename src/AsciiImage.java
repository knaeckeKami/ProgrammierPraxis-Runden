import java.util.Arrays;


/**
 * Created by mkamleithner on 10/24/14.
 */
public class AsciiImage {


    //the ascii image as one whole string without newline-characters
    private char[][] asciiImage;

    /**
     * creates a new, empty AsciiImage instance
     */

    public AsciiImage() {

    }

    /**
     * creates an AsciiImage from a String[]
     *
     * @param lines
     */
    public AsciiImage(String[] lines) {
        this.asciiImage = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            this.asciiImage[i] = lines[i].toCharArray();
        }

    }

    public AsciiImage(int width, int height) {
        this.asciiImage = new char[width][height];
        clear();
    }

    public void clear() {

        for (char[] anAsciiImage : this.asciiImage) {
            Arrays.fill(anAsciiImage, '.');
        }

    }

    public int getWidth() {
        return asciiImage.length;
    }

    public int getHeight() {
        return asciiImage[0].length;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        this.transpose();
        for (char[] anAsciiImage : this.asciiImage) {
            builder.append(anAsciiImage).append(System.lineSeparator());
        }
        //remove last \n
        builder.deleteCharAt(builder.length() - 1);
        this.transpose();
        return builder.toString();

    }

    /**
     * returns the character on position x/y.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the character on x/y
     * @throws AsciiShop.AsciiShopException if x/y are out of the boundaries of this image
     */
    public char getPixel(int x, int y) {
        if (isOutOfBoundaries(x, y)) {
            throw new AsciiShop.AsciiShopException(AsciiShop.ERRORS.OPERATION_FAILED.toString());
        }
        return this.asciiImage[x][y];
    }

    /**
     * sets the character on position x/y.
     *
     * @param x
     * @param y
     * @param c
     * @throws AsciiShop.AsciiShopException if x/y are out of the boundaries of this image
     */
    public void setPixel(int x, int y, char c) {
        if (isOutOfBoundaries(x, y)) {
            throw new AsciiShop.AsciiShopException(AsciiShop.ERRORS.OPERATION_FAILED.toString());
        }
        this.asciiImage[x][y] = c;
    }

    private boolean isOutOfBoundaries(int x, int y) {
        return x < 0 || y < 0 || x >= asciiImage.length || y >= asciiImage[0].length;
    }

    /**
     * replaces the character on position x/y with c, and recursively all neighbour characters
     * which do not match c.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param c that character which should replace the old char at x/y
     * @throws AsciiShop.AsciiShopException in case of invalid x/y
     */
    public void fill(int x, int y, char c) {
        //check if this character already matches this character
        // or throw exception in case of invalid x/y
        if ((this.getPixel(x, y) == c)) {
            //nothing to do
            return;
        }


        //save the old character for later
        char oldChar = this.getPixel(x, y);
        //replace the char on position x/y with the desired character
        this.setPixel(x, y, c);
        //check left neighbour
        if (x != 0 && getPixel(x - 1, y) == oldChar) {
            fill(x - 1, y, c);
        }
        //check above neighbour
        if (y != 0 && getPixel(x, y - 1) == oldChar) {
            fill(x, y - 1, c);
        }
        //check right neighbour
        if (x < this.getWidth() - 1 && getPixel(x + 1, y) == oldChar) {
            fill(x + 1, y, c);
        }
        //check down neighbour
        if (y < this.getHeight() - 1 && getPixel(x, y + 1) == oldChar) {
            fill(x, y + 1, c);
        }

    }

    /**
     * transposes the image.
     */
    public void transpose() {
        //create a new asciiimage with the dimensions of the old image, but width and height swapped
        AsciiImage help = new AsciiImage(this.getHeight(), this.getWidth());

        //get the char on position x,y on the original image and set it on the new image on position y/x
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                help.setPixel(y, x, getPixel(x, y));
            }
        }
        //take the state of the new image and copy it to new original image
        this.asciiImage = help.asciiImage;


    }

    /**
     * returns the lineIndex_th line. does not validate the input.
     *
     * @param lineIndex
     * @return
     * @throws java.lang.IndexOutOfBoundsException if lineIndex is not valid
     */
    private char[] getLine(int lineIndex) {
        return this.asciiImage[lineIndex];
    }


    /**
     * returns, if every line is this image is a palindrome.
     *
     * @return true/false
     */
    public boolean isSymmetricHorizontal() {
        for (int i = 0; i < this.getHeight(); i++) {
            if (!this.isLineHorizontal(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * returns, if the line at lineIndex is a palindrome
     *
     * @param lineIndex
     * @return
     */
    private boolean isLineHorizontal(int lineIndex) {
        char[] line = this.getLine(lineIndex);
        int lineLength = line.length;
        //only check to lineLength / 2 to avoid double-checks
        //**.**
        //01234
        //element 0 is compared to element 4
        //...     1                        3
        //element 2 is not compared to anything.
        for (int i = 0; i < lineLength / 2; i++) {
            if (line[i] != line[(lineLength - 1 - i)]) {
                return false;
            }
        }
        return true;
    }


    public void replace(char oldChar, char newChar) {
        for (int i = 0; i < this.asciiImage.length; i++) {
            for (int j = 0; j < this.asciiImage[i].length; j++) {
                if (this.asciiImage[i][j] == oldChar) {
                    this.asciiImage[i][j] = newChar;
                }
            }
        }
    }

    public void drawLine(int x0, int y0, int x1, int y1, char c) {
        System.out.println("drawline " + x0 + " " + y0 + " " + x1 + " " + y1 + " " + c);
        boolean swapped = false;
        int help;
        int deltaX = x1 - x0;
        int deltaY = y1 - y0;
        //check if the axis are inverted
        if (Math.abs(deltaY) > Math.abs(deltaX)) {
            swapped = true;
            //swap all x with y
            help = deltaX;
            deltaX = deltaY;
            deltaY = help;
            help = x0;
            x0 = y0;
            y0 = help;
            help = x1;
            x1 = y1;
            y1 = help;

        }
        if (x1 < x0) {
            help = x0;
            x0 = x1;
            x1 = help;
            help = y0;
            y0 = y1;
            y1 = help;

        }
        double y = y0;
        System.out.println("x0: " + x0 + " x1: " + x1 + " y0: " + y0 + " y1: " + y1);
        for (int x = x0; x <= x1; x++, y += (((double) deltaY) / deltaX)) {
            if (swapped) {
                this.asciiImage[(int) Math.round(y)][x] = c;
            } else
                this.asciiImage[x][(int) Math.round(y)] = c;

        }


    }


}