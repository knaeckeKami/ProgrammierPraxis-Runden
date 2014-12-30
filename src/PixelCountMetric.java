/**
 * Created by mkamleithner on 12/30/14.
 */
public class PixelCountMetric implements Metric<AsciiImage> {

    public int distance(AsciiImage o1, AsciiImage o2) {
        return Math.abs(size(o1) - size(o2));
    }

    private int size(AsciiImage img) {
        return img.getWidth() * img.getHeight();
    }

}
