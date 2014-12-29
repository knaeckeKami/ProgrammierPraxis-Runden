/**
 * Class, representing a point in an AsciiImage.
 * Immutable.
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
