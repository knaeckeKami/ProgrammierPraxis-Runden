import java.util.Arrays;

/**
 * Created by mkamleithner on 11/29/14.
 */
public class AsciiStack {

    private final int increment;
    private AsciiImage[] stack;
    private int position = -1;

    public AsciiStack(int increment) {
        this.increment = increment;
        this.stack = new AsciiImage[increment];
    }

    public int capacity() {
        return this.stack.length;
    }

    public boolean empty() {
        return this.position == -1;
    }

    public AsciiImage pop() {
        AsciiImage peeked = peek();
        if (peeked != null) {
            this.position--;
        }
        return peeked;
    }

    public AsciiImage peek() {
        if (this.position == -1) {
            return null;
        }
        return stack[position];
    }

    public void push(AsciiImage img) {
        if (position >= stack.length - 1) {
            this.stack = Arrays.copyOf(this.stack, this.stack.length + increment);
        }
        this.stack[++position] = img;

    }

    public int size() {
        return this.position + 1;
    }


}
