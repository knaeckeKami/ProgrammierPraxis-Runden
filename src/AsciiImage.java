import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by mkamleithner on 10/24/14.
 */
public class AsciiImage {

    /**
     * string of allowed characters in this image.
     * the first character is treated is the darkest character,
     * the last as the brightest one.
     */
    private final String charsetAsString;
    /**
     * the ascii image as one whole char[][] without newline-characters
     */
    private char[][] asciiImage;

    /**
     * copy constructor.
     * Creates a new AsciiImage, which is a deep copy of the given one.
     *
     * @param img the object to create a copy of
     */
    public AsciiImage(AsciiImage img) {

        this(img.getWidth(), img.getHeight(), img.getCharset());
        for (int i = 0; i < this.asciiImage.length; i++) {
            System.arraycopy(img.asciiImage[i], 0, this.asciiImage[i], 0, this.asciiImage[i].length);
        }
    }

    /**
     * creates a new AsciiImage with the specified width, height and charset.
     *
     * @param width
     * @param height
     * @param charset
     */
    public AsciiImage(int width, int height, String charset) {
        this.asciiImage = new char[width][height];

        this.charsetAsString = charset;
        fillWithBackgroundCharacter();
    }

    /**
     * returns the  bachground-character of this image.
     * This is equal to accessing the last character of the
     * charset of this AsciiImage.
     *
     * @return
     */
    public char getBackgroundCharacter() {
        return charsetAsString.charAt(charsetAsString.length() - 1);
    }

    /**
     * returns the charset of this AsciiImage.
     *
     * @return
     */
    public String getCharset() {
        return charsetAsString;
    }


    private void fillWithBackgroundCharacter() {

        for (char[] anAsciiImage : this.asciiImage) {
            Arrays.fill(anAsciiImage, getBackgroundCharacter());
        }

    }

    /**
     * returns the width of this image
     *
     * @return
     */
    public int getWidth() {
        return asciiImage.length;
    }

    /**
     * returns the height of this image
     *
     * @return
     */
    public int getHeight() {
        return asciiImage[0].length;
    }

    /**
     * returns a string represantation of this image
     *
     * @return
     */
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
        if (charsetAsString.indexOf(c) == -1) {
            throw new IndexOutOfBoundsException(c + "is not in the charset " + charsetAsString);
        }
        this.asciiImage[x][y] = c;
    }

    /**
     * returns an IndexOutOfBoundsException with an description,
     * why the given coordinates are not valid,
     *
     * @param x
     * @param y
     * @return
     */
    private IndexOutOfBoundsException getOutOfBoundsException(int x, int y) {
        return new IndexOutOfBoundsException("the pixel at " + x + "/" + y + " is out of the boundaries" +
                "of this image (" + getWidth() + "/" + getHeight() + ")");
    }


    /**
     * sets the pixel at the given point to the given character
     *
     * @param p
     * @param c
     * @throws java.lang.IndexOutOfBoundsException in case of an invalid point.
     */
    public void setPixel(AsciiPoint p, char c) {
        this.setPixel(p.getX(), p.getY(), c);
    }

    private boolean isOutOfBoundaries(int x, int y) {
        return x < 0 || y < 0 || x >= asciiImage.length || y >= asciiImage[0].length;
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
     * returns all direct (not diagonal) neighbours of the point x/y
     * aaaaa
     * aaXaa
     * aXpXa
     * aaXaa
     * aaaaa
     * On a given point p, all points marked as X will be returned.
     *
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
     * returns all direct (not diagonal) neighbours of the given point.
     *
     * @return Set containing all neighbours of the given point. can contain up to 4 elements. can be empty.
     */
    public Set<AsciiPoint> get4Neighbors(AsciiPoint point) {
        return get4Neighbors(point.getX(), point.getY());
    }

    /**
     * returns all (direct and diagonal) neighbours of the given point.
     * aaaaa
     * aXXXa
     * aXpXa
     * aXXXa
     * aaaaa
     * On a given point p, all points marked as "X" will be returned
     *
     * @param x
     * @param y
     * @return
     */
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

    /**
     * returns all (direct and diagonal) neighbours of the given point.
     * aaaaa
     * aXXXa
     * aXpXa
     * aXXXa
     * aaaaa
     * On a given point p, all points marked as "X" will be returned
     *
     * @param p AsciiPoint
     * @return set of neighbours
     */
    public Set<AsciiPoint> get8Neighbors(AsciiPoint p) {
        return get8Neighbors(p.getX(), p.getY());
    }

    public int getUniqueChars() {
        Set<Character> characters = new HashSet<Character>();
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                characters.add(getPixel(x, y));
            }
        }
        return characters.size();

    }

    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) {
            return false;
        }
        AsciiImage otherImage = (AsciiImage) other;
        if (this.getHeight() != otherImage.getHeight()) {
            return false;
        }
        if (this.getWidth() != otherImage.getWidth()) {
            return false;
        }
        return Arrays.deepEquals(this.asciiImage, otherImage.asciiImage);


    }


}