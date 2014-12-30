/**
 * Created by mkamleithner on 11/29/14.
 */
public class AsciiStack {

    private AsciiStackNode head;

    /**
     * returns the current size of the stack
     *
     * @return
     */
    public int size() {
        return empty() ? 0 : head.size();
    }

    /**
     * returns true iff the stack is empty, false otherwise.
     * @return
     */
    public boolean empty() {
        return head == null;
    }

    /**
     * pushes the given element on the stack
     * @param image
     */
    public void push(AsciiImage image) {
        AsciiStackNode node = new AsciiStackNode(image, null);
        if (empty()) {
            head = node;
        } else head.last().next = node;

    }

    /**
     * returns the topmost element and removes it from the stack.
     * returns null if the stack is empty.
     * @return
     */
    public AsciiImage pop() {
        if (empty()) return null;
        if (this.size() == 1) {
            AsciiImage img = head.image;
            head = null;
            return img;
        }
        AsciiStackNode currentElement = head;
        AsciiImage image = null;
        while (image == null) {
            if (currentElement.next.next == null) {
                image = currentElement.next.image;
                currentElement.next = null;

            }
            currentElement = currentElement.next;
        }
        return image;
    }

    /**
     * like pop, but doest not remove the element.
     * returns the topmost element.
     * returns null if the stack is empty.
     * @return
     */
    public AsciiImage peek() {
        if (empty()) return null;

        AsciiStackNode currentElement = head;
        while (currentElement.next != null) {
            currentElement = currentElement.next;
        }
        return currentElement.image;
    }

    /**
     * internal class for the implementation of the stack.
     * essentially a linked list
     */
    private class AsciiStackNode {

        private AsciiImage image;
        private AsciiStackNode next;

        public AsciiStackNode(AsciiImage image, AsciiStackNode next) {
            this.image = image;
            this.next = next;
        }

        public int size() {
            return 1 + ((next == null) ? 0 : next.size());
        }

        private AsciiStackNode last() {
            if (next == null) {
                return this;
            } else return next.last();
        }

    }

}
