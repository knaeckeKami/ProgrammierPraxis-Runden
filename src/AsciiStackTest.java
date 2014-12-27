/**
 * Created by mkamleithner on 11/29/14.
 */
public class AsciiStackTest {

    public static void main(String[] args) {
        AsciiStack stack = new AsciiStack();

        printState(stack);
        AsciiImage image = new AsciiImage(3, 3, "a");
        stack.push(image);
        printState(stack);
        stack.push(image);
        stack.push(image);
        stack.peek();
        printState(stack);


    }

    public static void printState(AsciiStack stack) {
        System.out.println("size:" + stack.size());
        System.out.println("empty:" + stack.empty());
        System.out.println("peek:" + stack.peek());


    }
}
