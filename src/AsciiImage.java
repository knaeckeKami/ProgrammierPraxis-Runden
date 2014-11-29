import java.util.*;


/**
 * Created by mkamleithner on 10/24/14.
 */
public class AsciiImage {


    private static final char BACKGROUND_CHARACTER = '.';
    //the ascii image as one whole char[][] without newline-characters
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
        if (width <= 0 || height <= 0) {
            throw new AsciiShop.AsciiShopException(AsciiShop.ERRORS.INPUT_ERROR.toString());
        }
        this.asciiImage = new char[width][height];
        clear();
    }

    public AsciiImage(AsciiImage img) {
        this.asciiImage = img.asciiImage.clone();
        for (int i = 0; i < asciiImage.length; i++) {
            asciiImage[i] = img.asciiImage[i].clone();
        }
    }

    public void clear() {

        for (char[] anAsciiImage : this.asciiImage) {
            Arrays.fill(anAsciiImage, BACKGROUND_CHARACTER);
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
            builder.append(anAsciiImage).append(System.getProperty("line.separator"));
        }

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
     * @throws AsciiShop.AsciiShopException if x/y are out of the boundaries of this image
     */
    public void setPixel(int x, int y, char c) {
        if (isOutOfBoundaries(x, y)) {
            throw new AsciiShop.AsciiShopException(AsciiShop.ERRORS.OPERATION_FAILED.toString());
        }
        this.asciiImage[x][y] = c;
    }

    /**
     * returns the centroid of the character c.
     */
    public AsciiPoint getCentroid(char c) {
        Collection<AsciiPoint> points = getPointList(c);

        if (points.isEmpty()) {
            return null;
        }

        int amountOfPixels = points.size();
        double sumX = 0;
        double sumY = 0;
        for (AsciiPoint point : points) {
            sumX += point.getX();
            sumY += point.getY();
        }
        return new AsciiPoint((int) Math.round(sumX / amountOfPixels), (int) Math.round(sumY / amountOfPixels));

    }


    public void setPixel(AsciiPoint p, char c) {
        this.setPixel(p.getX(), p.getY(), c);
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

        //check all neighbours
        Set<AsciiPoint> points = getNeighbors(x, y);
        for (AsciiPoint point : points) {
            if (getPixel(point) == oldChar) {
                fill(point.getX(), point.getY(), c);
            }
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
    public Set<AsciiPoint> getNeighbors(int x, int y) {

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
    public Set<AsciiPoint> getNeighbors(AsciiPoint point) {
        return getNeighbors(point.getX(), point.getY());
    }


    /**
     * grows the region of all points with the character c. All characters, which are neighbours of any point containing
     * c, which are set to BACKGROUND_CHARACTER, will be set to c.
     * @param c
     */
    public void growRegion(char c) {
        for (AsciiPoint point : getPointList(c)) {
            for (AsciiPoint neighbour : getNeighbors(point)) {
                if (getPixel(neighbour) == BACKGROUND_CHARACTER) {
                    setPixel(neighbour, c);
                }
            }
        }
    }


}