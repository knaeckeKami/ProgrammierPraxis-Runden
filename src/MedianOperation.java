import java.util.Arrays;

/**
 * Created by mkamleithner on 12/27/14.
 */
public class MedianOperation extends FilterOperation {


    @Override
    public int filter(int[] values) {
        Arrays.sort(values);
        return values[values.length / 2];
    }


}
