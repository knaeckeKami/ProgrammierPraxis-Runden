/**
 * Created by mkamleithner on 12/30/14.
 */
public class PixelCountMetric implements Metric<AsciiImage> {

    private static int size(AsciiImage img) {
        return img.getWidth() * img.getHeight();
    }

    /**
     * returns the difference between the areas of two images o1,o2.
     *
     * @param o1 the first argument
     * @param o2 the second argument
     * @return
     */
    public int distance(AsciiImage o1, AsciiImage o2) {
        return Math.abs(size(o1) - size(o2));
    }

}
