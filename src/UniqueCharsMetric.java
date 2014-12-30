/**
 * Created by mkamleithner on 12/30/14.
 */
public class UniqueCharsMetric implements Metric<AsciiImage> {


    /**
     * returns the difference between the count if unique chars of two images
     *
     * @param o1 the first argument
     * @param o2 the second argument
     * @return
     */
    public int distance(AsciiImage o1, AsciiImage o2) {
        return Math.abs(o1.getUniqueChars() - o2.getUniqueChars());
    }
}
