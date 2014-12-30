/**
 * Created by mkamleithner on 12/30/14.
 */
public class AverageOperation extends FilterOperation {


    /**
     * returns the arithmetic mean of the given int[]
     *
     * @param values int array
     * @return arithmetic mean
     * @throws java.lang.ArithmeticException if the array has the length 0
     */
    @Override
    public int filter(int[] values) {
        int sum = 0;
        for (int i : values) {
            sum += i;
        }
        return (int) Math.round(1.0 * sum / values.length);
    }
}
