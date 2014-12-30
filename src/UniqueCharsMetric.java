/**
 * Created by mkamleithner on 12/30/14.
 */
public class UniqueCharsMetric implements Metric<AsciiImage> {

    public int distance(AsciiImage o1, AsciiImage o2) {
        return Math.abs(o1.getUniqueChars() - o2.getUniqueChars());
    }
}
