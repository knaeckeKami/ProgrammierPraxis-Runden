import java.util.Set;

/**
 * Created by mkamleithner on 12/27/14.
 */
public class FillOperation implements Operation {

    private int x, y;
    private char charToReplace;

    public FillOperation(int x, int y, char charToReplace) {
        this.x = x;
        this.y = y;
        this.charToReplace = charToReplace;
    }

    private static void fill(AsciiImage img, int x, int y, char charToReplace) {
        //check if this character already matches this character
        // or throw exception in case of invalid x/y
        if ((img.getPixel(x, y) == charToReplace)) {
            //nothing to do
            return;
        }
        //save the old character for later
        char oldChar = img.getPixel(x, y);
        //replace the char on position x/y with the desired character
        img.setPixel(x, y, charToReplace);

        //check all neighbours
        Set<AsciiPoint> points = img.get4Neighbors(x, y);
        for (AsciiPoint point : points) {
            if (img.getPixel(point) == oldChar) {
                fill(img, point.getX(), point.getY(), charToReplace);
            }
        }
    }

    /**
     * replaces the character on position x/y with c, and recursively all neighbour characters
     * which do not match c.
     *
     * @throws OperationException in case of invalid x/y
     */
    public AsciiImage execute(AsciiImage img) throws OperationException {
        AsciiImage newImage = new AsciiImage(img);
        fill(newImage, x, y, charToReplace);
        return newImage;

    }

}
