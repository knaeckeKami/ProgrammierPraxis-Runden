/**
 * Created by mkamleithner on 11/29/14.
 */
public class AsciiPoint {

    private final int x, y;

    public AsciiPoint(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("(");
        builder.append(x).append(',').append(y).append(')');
        return builder.toString();
    }
}
