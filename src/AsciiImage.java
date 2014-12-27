import java.util.*;


/**
 * Created by mkamleithner on 10/24/14.
 */
public class AsciiImage {


    private final List<Character> charset = new ArrayList<Character>();
    private final String charsetAsString;
    //the ascii image as one whole char[][] without newline-characters
    private char[][] asciiImage;


    public AsciiImage(AsciiImage img) {
        this(img.getWidth(), img.getHeight(), img.getCharset());
    }

    public AsciiImage(int width, int height, String charset) {
        this.asciiImage = new char[width][height];
        for (char c : charset.toCharArray()) {
            this.charset.add(c);
        }
        this.charsetAsString = charset;
        clear();
    }

    public char getBackgroundCharacter() {
        return charset.get(charset.size() - 1);
    }


    public String getCharset() {
        return charsetAsString;
    }


    public void clear() {

        for (char[] anAsciiImage : this.asciiImage) {
            Arrays.fill(anAsciiImage, getBackgroundCharacter());
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
        for (char[] anAsciiImage : this.asciiImage) {
            builder.append(anAsciiImage).append(System.getProperty("line.separator"));
        }
        return builder.toString();

    }

    /**
     * returns the character on position x/y.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the character on x/y
     * @throws java.lang.IndexOutOfBoundsException if x/y are out of the boundaries of this image
     */
    public char getPixel(int x, int y) {
        if (isOutOfBoundaries(x, y)) {
            throw getOutOfBoundsException(x, y);
        }
        return this.asciiImage[x][y];
    }

    /**
     * returns the character on the given point.
     *
     * @param p
     * @return
     */
    public char getPixel(AsciiPoint p) {
        return this.getPixel(p.getX(), p.getY());
    }

    /**
     * sets the character on position x/y.
     *
     * @param x
     * @param y
     * @param c
     * @throws java.lang.IndexOutOfBoundsException if x/y are out of the boundaries of this image
     */
    public void setPixel(int x, int y, char c) {
        if (isOutOfBoundaries(x, y)) {
            throw getOutOfBoundsException(x, y);
        }
        if (!charset.contains(c)) {
            throw new IndexOutOfBoundsException(c + "is not in the charset " + charsetAsString);
        }
        this.asciiImage[x][y] = c;
    }

    private IndexOutOfBoundsException getOutOfBoundsException(int x, int y) {
        return new IndexOutOfBoundsException("the pixel at " + x + "/" + y + " is out of the boundaries" +
                "of this image (" + getWidth() + "/" + getHeight() + ")");
    }



    public void setPixel(AsciiPoint p, char c) {
        this.setPixel(p.getX(), p.getY(), c);
    }

    private boolean isOutOfBoundaries(int x, int y) {
        return x < 0 || y < 0 || x >= asciiImage.length || y >= asciiImage[0].length;
    }

    /*
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

    /**
     * replaces all occurrences  of oldChar with newChar
     */
    public void replace(char oldChar, char newChar) {
        for (int i = 0; i < this.asciiImage.length; i++) {
            for (int j = 0; j < this.asciiImage[i].length; j++) {
                if (this.asciiImage[i][j] == oldChar) {
                    this.asciiImage[i][j] = newChar;
                }
            }
        }
    }

    /**
     * draws a line of c-characters from x0/y0 to x1/y1 using the DDA algorithm
     * @param x0 the x coordinate where to start
     * @param y0 the y coordinate where to start
     * @param x1 the x coordinate where to end
     * @param y1 the y coordinate where to end
     * @param c  the character of the line
     */
    public void drawLine(int x0, int y0, int x1, int y1, char c) {
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
        for (int x = x0; x <= x1; x++, y += (((double) deltaY) / deltaX)) {
            if (swapped) {
                this.asciiImage[(int) Math.round(y)][x] = c;
            } else
                this.asciiImage[x][(int) Math.round(y)] = c;

        }
    }

    /**
     * returns all points, which are set to c.
     */
    public ArrayList<AsciiPoint> getPointList(char c) {
        ArrayList<AsciiPoint> pointList = new ArrayList<AsciiPoint>();
        for (int x = 0; x < this.asciiImage.length; x++) {
            for (int y = 0; y < this.asciiImage[x].length; y++) {
                if (asciiImage[x][y] == c) {
                    pointList.add(new AsciiPoint(x, y));
                }
            }

        }
        return pointList;
    }

    /**
     * returns all neighbours of the point x/y
     * @param x the x coordinate
     * @param y the y coordinate
     * @return Set containing all neighbours of x/y. can contain up to 4 elements. can be empty.
     */
    public Set<AsciiPoint> get4Neighbors(int x, int y) {

        Set<AsciiPoint> points = new HashSet<AsciiPoint>();
        //check left neighbour
        if (x != 0) {
            points.add(new AsciiPoint(x - 1, y));
        }
        //check above neighbour
        if (y != 0) {
            points.add(new AsciiPoint(x, y - 1));
        }
        //check right neighbour
        if (x < this.getWidth() - 1) {
            points.add(new AsciiPoint(x + 1, y));
        }
        //check down neighbour
        if (y < this.getHeight() - 1) {
            points.add(new AsciiPoint(x, y + 1));
        }
        return points;
    }


    /**
     * returns all neighbours of the given point.
     * @return Set containing all neighbours of the given point. can contain up to 4 elements. can be empty.
     */
    public Set<AsciiPoint> get4Neighbors(AsciiPoint point) {
        return get4Neighbors(point.getX(), point.getY());
    }

    public Set<AsciiPoint> get8Neighbors(int x, int y) {

        Set<AsciiPoint> points = get4Neighbors(x, y);

        if (x > 0 && y > 0) {
            points.add(new AsciiPoint(x - 1, y - 1));
        }

        if (x > 0 && y < getHeight() - 1) {
            points.add(new AsciiPoint(x - 1, y + 1));
        }

        if (x < this.getWidth() - 1 && y < getHeight() - 1) {
            points.add(new AsciiPoint(x + 1, y + 1));
        }

        if (x < this.getWidth() - 1 && y > 0) {
            points.add(new AsciiPoint(x + 1, y - 1));
        }

        return points;

    }

    public Set<AsciiPoint> get8Neighbors(AsciiPoint p) {
        return get8Neighbors(p.getX(), p.getY());
    }


}